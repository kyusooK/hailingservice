package paymentsystem.domain;

import lombok.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CancelledCompleted extends AbstractEvent {

    private Long id;
    private Long itemId;
    private String paymentId; // paymentId
    private String reason;
    private String status;

    public CancelledCompleted(Payment aggregate) {
        super(aggregate);
    }

    public CancelledCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
