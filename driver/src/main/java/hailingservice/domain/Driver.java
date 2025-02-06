package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.DriverApplication;
import hailingservice.domain.DriverDisapproved;
import hailingservice.domain.DriverRegistered;
import hailingservice.domain.HailingRejected;
import hailingservice.domain.Tmap;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "Driver_table")
@Data
//<<< DDD / Aggregate Root
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String driverLicenseNumber;

    private Boolean isApproved;

    private Boolean isHailing;

    private String driverLocation;

    private String operationRequestForm;

    private String operationInfo;

    private Long operationRequestId;

    @PostPersist
    public void onPostPersist() {
        DriverRegistered driverRegistered = new DriverRegistered(this);
        driverRegistered.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        DriverDisapproved driverDisapproved = new DriverDisapproved(this);
        driverDisapproved.publishAfterCommit();
    }

    public static DriverRepository repository() {
        DriverRepository driverRepository = DriverApplication.applicationContext.getBean(
            DriverRepository.class
        );
        return driverRepository;
    }

    //<<< Clean Arch / Port Method
    public void confirmLicense() {
        //implement business logic here:
        repository().findById(this.getId()).ifPresent(driver -> {
            if (driver.getDriverLicenseNumber() != null && driver.getDriverLicenseNumber().length() == 12) {
                driver.setIsApproved(true);
                repository().save(driver);
                
                DriverApproved driverApproved = new DriverApproved(driver);
                driverApproved.publishAfterCommit();
            }else{
                driver.setIsApproved(false);
                repository().save(driver);

                DriverDisapproved driverDisapproved = new DriverDisapproved(this);
                driverDisapproved.publishAfterCommit();
            }
        });
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public void changeOperationstatus(ChangeOperationstatusCommand changeOperationstatusCommand) {
        repository().findById(this.getId()).ifPresent(driver -> {
            driver.setIsHailing(changeOperationstatusCommand.getIsHailing());
            driver.setDriverLocation(changeOperationstatusCommand.getDriverLocation());

            repository().save(driver);

            OperationStatusChanged operationStatusChanged = new OperationStatusChanged(this);
            operationStatusChanged.publishAfterCommit();
        });
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void hailDriver(GpsBasedLocationConfirmed gpsBasedLocationConfirmed) {
        ObjectMapper mapper = new ObjectMapper();
        String passengerLocation = gpsBasedLocationConfirmed.getPassengerLocation();
        String apiKey = "BIwUJL1VBo3lanAgKYxGQ7egeR1SP8iD7UqIbYpN"; // API 키

        // 1. isHailing이 true인 모든 드라이버 조회
        List<Driver> availableDrivers = repository().findByIsHailingTrue();
        
        Driver closestDriver = null;
        double closestDistance = Double.MAX_VALUE;

        // 2. 각 드라이버의 위치와 승객 위치 간의 거리 계산
        for (Driver driver : availableDrivers) {
            // 드라이버의 위치를 좌표로 변환
            try {
                // 드라이버의 위치를 좌표로 변환
                JsonNode driverCoordinates = Tmap.convertAddressToCoordinate(driver.getDriverLocation(), apiKey);
                double driverLat = driverCoordinates.get("noorLat").asDouble();
                double driverLon = driverCoordinates.get("noorLon").asDouble();
    
                // 승객의 위치를 좌표로 변환
                JsonNode passengerCoordinates = Tmap.convertAddressToCoordinate(passengerLocation, apiKey);
                double passengerLat = passengerCoordinates.get("noorLat").asDouble();
                double passengerLon = passengerCoordinates.get("noorLon").asDouble();
    
                // 거리 계산
                JsonNode routeProperties = Tmap.calculateRoute(passengerLat, passengerLon, driverLat, driverLon, apiKey);
                double distance = routeProperties.get("totalDistance").asDouble();
    
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestDriver = driver;
                }
            } catch (Exception e) {
                // 예외 처리: 로그를 남기거나, 예외를 무시할 수 있습니다.
                System.err.println("Error processing driver location: " + e.getMessage());
            }
        }

        // 3. 가장 가까운 드라이버에게 operationRequestForm 정보 저장
        if (closestDriver != null) {
            // 해당 드라이버를 repository에서 다시 조회하여 업데이트
            Driver driverToUpdate = repository().findById(closestDriver.getId()).orElse(null);
            if (driverToUpdate != null) {
                driverToUpdate.setOperationRequestForm(
                    "승객 위치: " +  gpsBasedLocationConfirmed.getPassengerLocation() +
                    "목적지: " + gpsBasedLocationConfirmed.getDestination() +
                    "예상 거리: " + gpsBasedLocationConfirmed.getEstimatedDistance() +
                    "예상 시간: " + gpsBasedLocationConfirmed.getEstimatedTime()
                );
                repository().save(driverToUpdate);
            }
        }
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void acceptCarhailing(AcceptCarhailingCommand acceptCarhailingCommand) {

        repository().findById(this.getId()).ifPresent(driver -> {
            if(driver.getIsHailing() == true){

                driver.setIsHailing(false);
                repository().save(driver);

                HailingAccepted hailingAccepted = new HailingAccepted(this);
                hailingAccepted.publishAfterCommit();

            }else{

                driver.setIsHailing(acceptCarhailingCommand.getIsHailing());
                repository().save(driver);

                HailingRejected hailingRejected = new HailingRejected(this);
                hailingRejected.publishAfterCommit();
            }
        });
      
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void servePassengerBoardingLocation(
        DriverMatched driverMatched
    ) {
        
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingMap = mapper.convertValue(driverMatched.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            // driver.setOperationInfo(출발지 ~ 운전자 현재위치간 거리 및 시간안내 관련 logic 추가)
            repository().save(driver);


         });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void serveDestination( DestinationCalculated destinationCalculated) {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingMap = mapper.convertValue(destinationCalculated.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            // driver.setOperationInfo(출발지 ~ 목적지 거리 및 시간안내 관련 logic 추가)
            repository().save(driver);

         });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
