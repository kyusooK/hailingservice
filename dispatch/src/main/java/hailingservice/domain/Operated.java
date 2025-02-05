package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Operated extends AbstractEvent {

    private Long id;
    private String passengerLocation;
    private String destination;
    private DriverId driverId;
    private UserId userId;
    private OperationStatus operationStatus;

    public Operated(Operation aggregate) {
        super(aggregate);
    }

    public Operated() {
        super();
    }
}
//>>> DDD / Domain Event
