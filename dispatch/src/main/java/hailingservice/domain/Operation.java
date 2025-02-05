package hailingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hailingservice.DispatchApplication;
import hailingservice.domain.CarHailing;
import hailingservice.domain.Operated;
import hailingservice.domain.OperationCompleted;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Operation_table")
@Data
//<<< DDD / Aggregate Root
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String passengerLocation;

    private String destination;

    @Enumerated(EnumType.STRING)
    private OperationStatus operationStatus;

    @Embedded
    private UserId userId;

    @Embedded
    private DriverId driverId;

    private Long fee;

    @PostPersist
    public void onPostPersist() {
        CarHailing carHailing = new CarHailing(this);
        carHailing.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        OperationCompleted operationCompleted = new OperationCompleted(this);
        operationCompleted.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        Operated operated = new Operated(this);
        operated.publishAfterCommit();
    }

    public static OperationRepository repository() {
        OperationRepository operationRepository = DispatchApplication.applicationContext.getBean(
            OperationRepository.class
        );
        return operationRepository;
    }
}
//>>> DDD / Aggregate Root
