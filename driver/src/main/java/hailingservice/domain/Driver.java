package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.DriverApplication;
import hailingservice.domain.DriverDisapproved;
import hailingservice.domain.DriverRegistered;
import hailingservice.domain.HailingRejected;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

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
    public static void hailDriver(
        GpsBasedLocationConfirmed gpsBasedLocationConfirmed
    ) {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingMap = mapper.convertValue(gpsBasedLocationConfirmed.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            driver.setOerationRequestForm(
                "승객위치" + ":" + gpsBasedLocationConfirmed.getPassengerLocation() + "\n"
                + "목적지" + ":" + gpsBasedLocationConfirmed.getDestination() + "\n")
            repository().save(driver);

         });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void acceptCarhailing(AcceptCarhailingCommand acceptCarhailingCommand) {

        repository().findById(this.getId()).ifPresent(driver -> {
            if(acceptCarhailingCommand.getIsHailing() == true){

                driver.setIsHailing(acceptCarhailingCommand.getIsHailing());
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
    public void changeOperationstatus(
        ChangeOperationstatusCommand changeOperationstatusCommand
    ) {
        //implement business logic here:

        OperationStatusChanged operationStatusChanged = new OperationStatusChanged(
            this
        );
        operationStatusChanged.publishAfterCommit();
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
        Map<Long, Object> matchingMap = mapper.convertValue(driverMatched.getDriverId(), Map.class);

        repository().findById(Long.valueOf(matchingMap.get("id").toString())).ifPresent(driver->{
            
            // driver.setOperationInfo(출발지 ~ 목적지 거리 및 시간안내 관련 logic 추가)
            repository().save(driver);

         });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
