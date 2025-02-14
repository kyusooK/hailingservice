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

    private String driverLocation;

    public static MatchingRepository repository() {
        MatchingRepository matchingRepository = DispatchApplication.applicationContext.getBean(
            MatchingRepository.class
        );
        return matchingRepository;
    }

    //<<< Clean Arch / Port Method
    public static void confirmGpsBasedLocation(CarHailing carHailing) {
            
            Matching matching = new Matching();
            matching.setPassengerLocation(carHailing.getPassengerLocation());
            matching.setDestination(carHailing.getDestination());
            matching.setUserId(carHailing.getUserId());
            
            repository().save(matching);
            
            GpsBasedLocationConfirmed gpsBasedLocationConfirmed = new GpsBasedLocationConfirmed(matching);
            gpsBasedLocationConfirmed.publishAfterCommit();
            
       
    }
    
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void matchDriver(HailingAccepted hailingAccepted) {
        ObjectMapper mapper = new ObjectMapper();
        String apiKey = "BIwUJL1VBo3lanAgKYxGQ7egeR1SP8iD7UqIbYpN"; // API 키

        repository().findById(hailingAccepted.getOperationRequestId()).ifPresent(matching->{

            try {
                // 승객의 위치 좌표 변환
                JsonNode passengerCoordinates = Tmap.convertAddressToCoordinate(matching.getPassengerLocation(), apiKey);
                double passengerLat = passengerCoordinates.get("noorLat").asDouble();
                double passengerLon = passengerCoordinates.get("noorLon").asDouble();
    
                // 드라이버의 위치 좌표 변환
                JsonNode driverCoordinates = Tmap.convertAddressToCoordinate(hailingAccepted.getDriverLocation(), apiKey);
                double driverLat = driverCoordinates.get("noorLat").asDouble();
                double driverLon = driverCoordinates.get("noorLon").asDouble();
    
                // 거리 및 시간 계산
                JsonNode routeProperties = Tmap.calculateRoute(passengerLat, passengerLon, driverLat, driverLon, apiKey);
                int estimatedTime = routeProperties.get("totalTime").asInt();
                int estimatedDistance = routeProperties.get("totalDistance").asInt();

                hailingservice.external.Reservation reservation = new hailingservice.external.Reservation();
                
                reservation.setTaskId(matching.getId().toString());
                reservation.setTitle("DriverMatched");
                reservation.setDescription(
                "운전자 위치: " + matching.getDriverLocation() + 
                "승객 위치: " + matching.getPassengerLocation() + 
                "소요 시간" + estimatedTime +
                "소요 거리" + estimatedDistance);
                reservation.setNow(true);

                DispatchApplication.applicationContext.getBean(hailingservice.external.ReservationService.class)
                    .createReservation(reservation);
    
                // 매칭 정보에 예상 시간 및 거리 저장
                matching.setDriverId(new DriverId(hailingAccepted.getId()));
                matching.setDriverLocation(hailingAccepted.getDriverLocation());
                matching.setEstimatedTime(estimatedTime);
                matching.setEstimatedDistance(estimatedDistance);

                repository().save(matching);
    
            } catch (Exception e) {
                // 예외 처리: 로그를 남기거나, 예외를 무시할 수 있습니다.
                System.err.println("Error calculating distance and time: " + e.getMessage());
            }

            DriverMatched driverMatched = new DriverMatched(matching);
            driverMatched.publishAfterCommit();

         });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void calculateDestination(Operated operated) {
        String apiKey = "BIwUJL1VBo3lanAgKYxGQ7egeR1SP8iD7UqIbYpN"; // API 키
        Long matchingId = Long.valueOf(operated.getId()) + 1;
        repository().findById(matchingId).ifPresent(matching->{
            
            try {
                // 승객의 위치 좌표 변환
                JsonNode passengerCoordinates = Tmap.convertAddressToCoordinate(matching.getPassengerLocation(), apiKey);
                double passengerLat = passengerCoordinates.get("noorLat").asDouble();
                double passengerLon = passengerCoordinates.get("noorLon").asDouble();
    
                // 드라이버의 위치 좌표 변환
                JsonNode driverCoordinates = Tmap.convertAddressToCoordinate(matching.getDestination(), apiKey);
                double driverLat = driverCoordinates.get("noorLat").asDouble();
                double driverLon = driverCoordinates.get("noorLon").asDouble();
    
                // 거리 및 시간 계산
                JsonNode routeProperties = Tmap.calculateRoute(passengerLat, passengerLon, driverLat, driverLon, apiKey);
                int estimatedTime = routeProperties.get("totalTime").asInt();
                int estimatedDistance = routeProperties.get("totalDistance").asInt();
    
                // 매칭 정보에 예상 시간 및 거리 저장
                matching.setDriverId(new DriverId(operated.getId()));
                matching.setDestination(operated.getDestination());
                matching.setEstimatedTime(estimatedTime);
                matching.setEstimatedDistance(estimatedDistance);

                repository().save(matching);
    
            } catch (Exception e) {
                // 예외 처리: 로그를 남기거나, 예외를 무시할 수 있습니다.
                System.err.println("Error calculating distance and time: " + e.getMessage());
            }

            DestinationCalculated destinationCalculated = new DestinationCalculated(matching);
            destinationCalculated.publishAfterCommit();

         });

    }

    

    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
