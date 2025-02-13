package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OperationCompleted extends AbstractEvent {

    private Long id;
    private DriverId driverId;
    private String destination;
    private OperationStatus operationStatus;
    private Long fee;
    private String paymentId;
    private String paymentStatus;

    public OperationCompleted(Operation aggregate) {
        super(aggregate);
    }

    public OperationCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
