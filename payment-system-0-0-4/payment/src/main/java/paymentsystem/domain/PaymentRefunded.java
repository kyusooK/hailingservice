package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import paymentsystem.domain.*;
import paymentsystem.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentRefunded extends AbstractEvent {

    private Long id;
    private String paymethod;
    private String storeId;
    private Integer price;
    private Date timeStamp;
    private String name;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;

    public PaymentRefunded(Payment aggregate) {
        super(aggregate);
    }

    public PaymentRefunded() {
        super();
    }
}
//>>> DDD / Domain Event
