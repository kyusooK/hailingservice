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
        
        //implement business logic here:
        

        hailingservice.external.OperationQuery operationQuery = new hailingservice.external.OperationQuery();
        // operationQuery.set??()        
          = OperationApplication.applicationContext
            .getBean(hailingservice.external.Service.class)
            .operation(operationQuery);

        Operated operated = new Operated(this);
        operated.publishAfterCommit();
    }
//>>> Clean Arch / Port Method

//<<< Clean Arch / Port Method
    public static void registerDriver(HailingAccepted hailingAccepted){
        
        //implement business logic here:
        
        /** Example 1:  new item 
        Operation operation = new Operation();
        repository().save(operation);

        */

        /** Example 2:  finding and process
        

        repository().findById(hailingAccepted.get???()).ifPresent(operation->{
            
            operation // do something
            repository().save(operation);


         });
        */

        
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
