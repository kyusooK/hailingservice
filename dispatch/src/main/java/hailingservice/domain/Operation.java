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

    private Long paymentId;
    
    private String paymentStatus;


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

    public static void registerDriver(HailingAccepted hailingAccepted){
        Long operationId = Long.valueOf(hailingAccepted.getOperationRequestId()) - 1;
        repository().findById(operationId).ifPresent(operation->{
            
            operation.setDriverId(new DriverId(hailingAccepted.getId()));
            repository().save(operation);
        });
    }

    public void operate(OperateCommand operateCommand){
        
        repository().findById(this.getId()).ifPresent(operation->{
            operation.setOperationStatus(operateCommand.getOperationStatus());

            Operated operated = new Operated(this);
            operated.publishAfterCommit();
        });
    }

    public void completeOperation(CompleteOperationCommand completeOperationCommand){
        
        repository().findById(this.getId()).ifPresent(operation->{

            operation.setFee(completeOperationCommand.getFee());
            operation.setOperationStatus(completeOperationCommand.getOperationStatus());

            OperationCompleted operationCompleted = new OperationCompleted(this);
            operationCompleted.publishAfterCommit();
        });
    }

    public static void updatePaymentInfo(PaymentCompleted paymentCompleted){
        
        repository().findById(paymentCompleted.getItemId()).ifPresent(operation->{
            
            operation.setPaymentId(paymentCompleted.getId());
            operation.setPaymentStatus("결제 완료");
            repository().save(operation);


         });

        
    }

}
//>>> DDD / Aggregate Root
