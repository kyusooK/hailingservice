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

        repository().findById(this.getId()).ifPresent(operation ->{
            if(operation.getOperationStatus() == OperationStatus.OPERATED){

                Operated operated = new Operated(this);
                operated.publishAfterCommit();

            }else if(operation.getOperationStatus() == OperationStatus.DONE){

                // driver.setFee(3800 + "출발지 ~ 목적지간 거리 x 200");

                OperationCompleted operationCompleted = new OperationCompleted(this);
                operationCompleted.publishAfterCommit();

            }
        });
    }

    public static OperationRepository repository() {
        OperationRepository operationRepository = DispatchApplication.applicationContext.getBean(
            OperationRepository.class
        );
        return operationRepository;
    }
}
//>>> DDD / Aggregate Root
