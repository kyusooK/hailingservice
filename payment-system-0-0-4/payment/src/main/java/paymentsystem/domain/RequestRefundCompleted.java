package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import paymentsystem.domain.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestRefundCompleted extends AbstractEvent {

    private Long id;
    private String paymethod;
    private String storeId;
    private Integer price;
    private Date timeStamp;
    private String name;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;

    public RequestRefundCompleted(Payment aggregate) {
        super(aggregate);
    }

    public RequestRefundCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
