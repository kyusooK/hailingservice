package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import paymentsystem.domain.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentCompleted extends AbstractEvent {

    private Long id;
    private Long itemId;
    private String paymentId; // PG 결제 번호
    private String status;
    private String reason;
    
    public PaymentCompleted(Payment aggregate) {
        super(aggregate);
    }

    public PaymentCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
