package hailingservice.domain;

import hailingservice.domain.CarHailing;
import hailingservice.domain.OperationCompleted;
import hailingservice.DispatchApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
@Table(name="Operation_table")
@Data

//<<< DDD / Aggregate Root
public class Operation  {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
    public void onPostPersist(){
        CarHailing carHailing = new CarHailing(this);
        carHailing.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
        OperationCompleted operationCompleted = new OperationCompleted(this);
        operationCompleted.publishAfterCommit();
    }

    public static OperationRepository repository(){
        OperationRepository operationRepository = DispatchApplication.applicationContext.getBean(OperationRepository.class);
        return operationRepository;
    }



//<<< Clean Arch / Port Method
    public void operate(OperateCommand operateCommand){
        
        repository().findById(this.getId()).ifPresent(operation->{
            operation.setOperationStatus(operateCommand.getOperationStatus());

            Operated operated = new Operated(this);
            operated.publishAfterCommit();
        });
    }
    public static void registerDriver(HailingAccepted hailingAccepted){
        
        repository().findById(hailingAccepted.getOperationRequestId() - 1).ifPresent(operation->{
            
            operation.setDriverId(new DriverId(hailingAccepted.getId()));
            repository().save(operation);
        });
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
