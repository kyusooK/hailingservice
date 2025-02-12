package hailingservice.domain;

import hailingservice.domain.DriverRegistered;
import hailingservice.domain.HailingRejected;
import hailingservice.domain.DriverDisapproved;
import hailingservice.DriverApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="Driver_table")
@Data

//<<< DDD / Aggregate Root
public class Driver  {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
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
    public void onPostPersist(){


        DriverRegistered driverRegistered = new DriverRegistered(this);
        driverRegistered.publishAfterCommit();
    }
    
    @PostUpdate
    public void onPostUpdate(){


        DriverDisapproved driverDisapproved = new DriverDisapproved(this);
        driverDisapproved.publishAfterCommit();

    
    }

    public static DriverRepository repository(){
        DriverRepository driverRepository = DriverApplication.applicationContext.getBean(DriverRepository.class);
        return driverRepository;
    }



//<<< Clean Arch / Port Method
    public void confirmLicense(){
        
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
        String passengerLocation = gpsBasedLocationConfirmed.getPassengerLocation();
        String apiKey = "BIwUJL1VBo3lanAgKYxGQ7egeR1SP8iD7UqIbYpN"; // API 키

        // 호출 요청 여부 상태가 true인 모든 운전자 조회
        List<Driver> availableDrivers = repository().findByIsHailingTrue();
        
        Driver closestDriver = null;
        double closestDistance = Double.MAX_VALUE;

        // 각 운전자의 현재 위치와 승객 위치 간의 거리 계산
        for (Driver driver : availableDrivers) {
            try {
                // 운전자 위치를 좌표로 변환
                JsonNode driverCoordinates = Tmap.convertAddressToCoordinate(driver.getDriverLocation(), apiKey);
                double driverLat = driverCoordinates.get("noorLat").asDouble();
                double driverLon = driverCoordinates.get("noorLon").asDouble();
    
                // 승객의 위치를 좌표로 변환
                JsonNode passengerCoordinates = Tmap.convertAddressToCoordinate(passengerLocation, apiKey);
                double passengerLat = passengerCoordinates.get("noorLat").asDouble();
                double passengerLon = passengerCoordinates.get("noorLon").asDouble();
    
                // 운전자 ~ 승객 간의 거리 계산
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

        // 가장 가까운 운전자를 찾은 후 호출요청서 정보 저장
        if (closestDriver != null) {
            // 해당 드라이버를 repository에서 다시 조회하여 업데이트
            Driver driver = repository().findById(closestDriver.getId()).orElse(null);
            if (driver != null) {
                driver.setOperationRequestId(gpsBasedLocationConfirmed.getId());
                driver.setOperationRequestForm(
                    "차량 호출 요청 정보입니다.\n" +
                    "승객 위치: " +  gpsBasedLocationConfirmed.getPassengerLocation() + "\n" +
                    "목적지: " + gpsBasedLocationConfirmed.getDestination());
                repository().save(driver);
            }
        }
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void acceptCarhailing() {

        repository().findById(this.getId()).ifPresent(driver -> {
            if(driver.getIsHailing() == true){

                driver.setIsHailing(false);
                repository().save(driver);

                HailingAccepted hailingAccepted = new HailingAccepted(this);
                hailingAccepted.publishAfterCommit();

            }else{

                driver.setIsHailing(false);
                repository().save(driver);

                HailingRejected hailingRejected = new HailingRejected(this);
                hailingRejected.publishAfterCommit();
            }
        });
      
    }
//>>> Clean Arch / Port Method

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void servePassengerBoardingLocation(DriverMatched driverMatched) {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingMap = mapper.convertValue(driverMatched.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            String formattedDistance = String.format("%.3f km", driverMatched.getEstimatedDistance() / 1000.0);
            String formattedTime = formatTime(driverMatched.getEstimatedTime());

            driver.setOperationInfo(
                "승객 위치 안내 정보입니다.\n" +
                "승객 위치: " + driverMatched.getPassengerLocation() + "\n" +
                "예상 거리: " + formattedDistance + "\n" +
                "예상 시간: " + formattedTime);
            repository().save(driver);
         });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void serveDestination( DestinationCalculated destinationCalculated) {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingMap = mapper.convertValue(destinationCalculated.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            String formattedDistance = String.format("%.3f km", destinationCalculated.getEstimatedDistance() / 1000.0);
            String formattedTime = formatTime(destinationCalculated.getEstimatedTime());

            driver.setOperationInfo(
                "목적지 안내 정보입니다.\n" +
                "출발 위치: " + destinationCalculated.getPassengerLocation() + "\n" +
                "목적지: " + destinationCalculated.getDestination() + "\n" +
                "예상 거리: " + formattedDistance + "\n" +
                "예상 시간: " + formattedTime);
            repository().save(driver);

         });

        
    }

    private static String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
    
        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append("시간 ");
        }
        if (minutes > 0 || hours > 0) {
            result.append(minutes).append("분 ");
        }
        result.append(seconds).append("초");
    
        return result.toString().trim();
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
