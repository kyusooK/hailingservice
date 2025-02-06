package hailingservice.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Tmap {

    public static JsonNode convertAddressToCoordinate(String address, String apiKey) throws Exception {
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        String geocodingUrl = "https://apis.openapi.sk.com/tmap/pois?version=1"
            + "&searchKeyword=" + encodedAddress
            + "&appKey=" + apiKey
            + "&count=1";
        
        URL url = new URL(geocodingUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.toString());
            return jsonResponse.get("searchPoiInfo")
                             .get("pois")
                             .get("poi")
                             .get(0);
        }
    }
    
    public static JsonNode calculateRoute(double startLat, double startLon, 
                                         double endLat, double endLon, 
                                         String apiKey) throws Exception {
        String urlStr = "https://apis.openapi.sk.com/tmap/routes?version=1";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("appKey", apiKey);
        conn.setDoOutput(true);
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("startX", startLon);
        requestBody.put("startY", startLat);
        requestBody.put("endX", endLon);
        requestBody.put("endY", endLat);
        requestBody.put("reqCoordType", "WGS84GEO");
        requestBody.put("resCoordType", "WGS84GEO");
        
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(requestBody.toString());
            writer.flush();
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            JsonNode jsonResponse = mapper.readTree(response.toString());
            return jsonResponse.get("features").get(0).get("properties");
        }
    }
}
