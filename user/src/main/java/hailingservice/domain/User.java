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
        
        
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> matchingUserMap = mapper.convertValue(driverMatched.getUserId(), Map.class);

        repository().findById(Long.valueOf(matchingUserMap.get("id").toString())).ifPresent(user->{
            
            user.setMessage(
                "차량 호출이 수락되었습니다.\n" +
                "기사님 위치: " + driverMatched.getDriverLocation() + "\n" +
                "도착 예상 시간: " + driverMatched.getEstimatedTime());
            repository().save(user);

        });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
