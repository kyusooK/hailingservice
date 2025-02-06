package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.DispatchApplication;
import hailingservice.domain.DestinationCalculated;
import hailingservice.domain.DriverMatched;
import hailingservice.domain.GpsBasedLocationConfirmed;
import hailingservice.domain.Tmap;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

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

    private Double latitude;

    private Double longitude;

    private Integer estimatedTime;

    private Integer estimatedDistance;

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
            
            // 1. 출발지 좌표 전환
            JsonNode passengerCoordinates = Tmap.convertAddressToCoordinate(carHailing.getPassengerLocation(), apiKey);
            double passengerLat = Double.parseDouble(passengerCoordinates.get("noorLat").asText());
            double passengerLon = Double.parseDouble(passengerCoordinates.get("noorLon").asText());
            
            // 2. 도착지 좌표 변환
            JsonNode driverCoordinates = Tmap.convertAddressToCoordinate(carHailing.getDestination(), apiKey);
            double driverLat = Double.parseDouble(driverCoordinates.get("noorLat").asText());
            double driverLon = Double.parseDouble(driverCoordinates.get("noorLon").asText());
            
            // 3. 경로 계산
            JsonNode routeProperties = Tmap.calculateRoute(passengerLat, passengerLon, driverLat, driverLon, apiKey);
            
            // 4. Matching 엔티티 생성 및 저장
            Matching matching = new Matching();
            matching.setPassengerLocation(carHailing.getPassengerLocation());
            matching.setDestination(carHailing.getDestination());
            matching.setUserId(carHailing.getUserId());
            matching.setEstimatedTime(routeProperties.get("totalTime").asInt());
            matching.setEstimatedDistance(routeProperties.get("totalDistance").asInt());
            
            repository().save(matching);
            
            // 5. 이벤트 발행
            GpsBasedLocationConfirmed gpsBasedLocationConfirmed = new GpsBasedLocationConfirmed(matching);
            gpsBasedLocationConfirmed.publishAfterCommit();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate route: " + e.getMessage());
        }
    }
    
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void matchDriver(HailingAccepted hailingAccepted) {

        repository().findById(hailingAccepted.getOperationRequestId()).ifPresent(matching->{

            drivet.setDriverId(hailingAccepted.getId());
            DriverMatched driverMatched = new DriverMatched(matching);
            driverMatched.publishAfterCommit();

         });

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
