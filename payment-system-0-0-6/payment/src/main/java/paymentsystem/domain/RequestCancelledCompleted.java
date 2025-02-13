package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import paymentsystem.domain.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestCancelledCompleted extends AbstractEvent {

    private Long id;
    private Long itemId;
    private String paymentId;
    private String name;
    private Integer price;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;

    public RequestCancelledCompleted(Payment aggregate) {
        super(aggregate);
    }

    public RequestCancelledCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
