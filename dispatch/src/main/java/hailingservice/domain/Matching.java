package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.DispatchApplication;
import hailingservice.domain.DestinationCalculated;
import hailingservice.domain.DriverMatched;
import hailingservice.domain.GpsBasedLocationConfirmed;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

// Tmap API Import
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Entity
@Table(name = "Matching_table")
@Data
//<<< DDD / Aggregate Root
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String destination;

    private String passengerLocation;

    @Embedded
    private DriverId driverId;

    @Embedded
    private UserId userId;

    private Integer estimatedTime;

    public static MatchingRepository repository() {
        MatchingRepository matchingRepository = DispatchApplication.applicationContext.getBean(
            MatchingRepository.class
        );
        return matchingRepository;
    }

    //<<< Clean Arch / Port Method
    public static void confirmGpsBasedLocation(CarHailing carHailing) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String apiKey = "BIwUJL1VBo3lanAgKYxGQ7egeR1SP8iD7UqIbYpN";
            
            // 1. 주소를 좌표로 변환
            GeoPoint passengerPoint = convertAddressToCoordinate(carHailing.getPassengerLocation(), apiKey);
            GeoPoint driverPoint = convertAddressToCoordinate("경기도 안산시 단원구 고잔동 527-1", apiKey); // carHailing.getDriverAddress()
            
            // 2. 경로 탐색 API 호출 설정
            String urlStr = "https://apis.openapi.sk.com/tmap/routes?version=1";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("appKey", apiKey);
            conn.setDoOutput(true);
            
            // 3. 요청 바디 구성 (변환된 좌표 사용)
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("startX", passengerPoint.getLongitude());
            requestBody.put("startY", passengerPoint.getLatitude());
            requestBody.put("endX", driverPoint.getLongitude());
            requestBody.put("endY", driverPoint.getLatitude());
            requestBody.put("reqCoordType", "WGS84GEO");
            requestBody.put("resCoordType", "WGS84GEO");
            
            // 4. API 요청 전송
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(requestBody.toString());
            writer.flush();
            
            // 5. 응답 처리
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            // 6. JSON 응답 파싱
            JsonNode jsonResponse = mapper.readTree(response.toString());
            JsonNode features = jsonResponse.get("features");
            JsonNode properties = features.get(0).get("properties");
            
            // 7. 소요 시간 추출 (초 단위)
            int totalTime = properties.get("totalTime").asInt();
            
            // 8. Matching 엔티티 생성 및 저장
            Matching matching = new Matching();
            // matching.setUserId(carHailing.getUserId());
            // matching.setDriverId(carHailing.getDriverId());
            matching.setPassengerLocation(carHailing.getPassengerLocation());
            matching.setEstimatedTime(totalTime); // 예상 소요시간 설정
            
            repository().save(matching);
            
            // 9. 이벤트 발행
            GpsBasedLocationConfirmed gpsBasedLocationConfirmed = new GpsBasedLocationConfirmed(matching);
            gpsBasedLocationConfirmed.publishAfterCommit();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate route: " + e.getMessage());
        }
    }

    private static GeoPoint convertAddressToCoordinate(String address, String apiKey) throws Exception {
        // 1. Geocoding API 호출 설정
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        String geocodingUrl = "https://apis.openapi.sk.com/tmap/pois?version=1"
        + "&searchKeyword=" + encodedAddress
        + "&appKey=" + apiKey
        + "&count=1";
        
        URL url = new URL(geocodingUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        // 2. 응답 처리
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        
        // 3. JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.toString());
        JsonNode poi = jsonResponse.get("searchPoiInfo")
                              .get("pois")
                              .get("poi")
                              .get(0);
        
        // 4. 좌표 추출
        double latitude = Double.parseDouble(poi.get("noorLat").asText());
        double longitude = Double.parseDouble(poi.get("noorLon").asText());
        
        return new GeoPoint(latitude, longitude);
    }
    
    // 좌표 정보를 담는 내부 클래스
    private static class GeoPoint {
        private final double latitude;
        private final double longitude;
        
        public GeoPoint(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        public double getLatitude() {
            return latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
    } 
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void matchDriver(HailingAccepted hailingAccepted) {
        //implement business logic here:

        /** Example 1:  new item 
        Matching matching = new Matching();
        repository().save(matching);

        DriverMatched driverMatched = new DriverMatched(matching);
        driverMatched.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(hailingAccepted.get???()).ifPresent(matching->{
            
            matching // do something
            repository().save(matching);

            DriverMatched driverMatched = new DriverMatched(matching);
            driverMatched.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void calculateDestination(Operated operated) {
        //implement business logic here:

        /** Example 1:  new item 
        Matching matching = new Matching();
        repository().save(matching);

        DestinationCalculated destinationCalculated = new DestinationCalculated(matching);
        destinationCalculated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if operated.userIddriverId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<Long, Object> operationMap = mapper.convertValue(operated.getUserId(), Map.class);
        // Map<Long, Object> operationMap = mapper.convertValue(operated.getDriverId(), Map.class);

        repository().findById(operated.get???()).ifPresent(matching->{
            
            matching // do something
            repository().save(matching);

            DestinationCalculated destinationCalculated = new DestinationCalculated(matching);
            destinationCalculated.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
