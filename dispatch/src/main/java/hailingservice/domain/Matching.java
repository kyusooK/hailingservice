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
    //<<< Clean Arch / Port Method
    public static void confirmGpsBasedLocation(CarHailing carHailing) {
        //implement business logic here:

        /** Example 1:  new item 
        Matching matching = new Matching();
        repository().save(matching);

        GpsBasedLocationConfirmed gpsBasedLocationConfirmed = new GpsBasedLocationConfirmed(matching);
        gpsBasedLocationConfirmed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if carHailing.userIddriverId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<Long, Object> operationMap = mapper.convertValue(carHailing.getUserId(), Map.class);
        // Map<Long, Object> operationMap = mapper.convertValue(carHailing.getDriverId(), Map.class);

        repository().findById(carHailing.get???()).ifPresent(matching->{
            
            matching // do something
            repository().save(matching);

            GpsBasedLocationConfirmed gpsBasedLocationConfirmed = new GpsBasedLocationConfirmed(matching);
            gpsBasedLocationConfirmed.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
