package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import paymentsystem.domain.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class RequstPaymentCompleted extends AbstractEvent {

    private Long id;
    private Integer price;
    private String name;
    private String buyerName;
    private String buyerId;
    private String buyerTel;
    private String buyerEmail;
    private String status;
    private String reason;


    public RequstPaymentCompleted(Payment aggregate) {
        super(aggregate);
    }

    public RequstPaymentCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
