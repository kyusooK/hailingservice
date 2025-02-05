package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.UserApplication;
import hailingservice.domain.UserRegistered;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String phoneNumber;

    private String message;

    @PostPersist
    public void onPostPersist() {
        UserRegistered userRegistered = new UserRegistered(this);
        userRegistered.publishAfterCommit();
    }

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public static void serveDriverInfo(DriverMatched driverMatched) {
        //implement business logic here:

        /** Example 1:  new item 
        User user = new User();
        repository().save(user);

        */

        /** Example 2:  finding and process
        
        // if driverMatched.tmapIddriverIduserId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> matchingMap = mapper.convertValue(driverMatched.getTmapId(), Map.class);
        // Map<Long, Object> matchingMap = mapper.convertValue(driverMatched.getDriverId(), Map.class);
        // Map<Long, Object> matchingMap = mapper.convertValue(driverMatched.getUserId(), Map.class);

        repository().findById(driverMatched.get???()).ifPresent(user->{
            
            user // do something
            repository().save(user);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
