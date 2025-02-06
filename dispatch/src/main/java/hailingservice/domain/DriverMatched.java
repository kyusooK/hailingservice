package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DriverMatched extends AbstractEvent {

    private Long id;
    private DriverId driverId;
    private String passengerLocation;
    private UserId userId;
    private Integer estimatedTime;
    private Integer estimatedDistance;
    private String destination;
    private String driverLocation;

    public DriverMatched(Matching aggregate) {
        super(aggregate);
    }

    public DriverMatched() {
        super();
    }
}
//>>> DDD / Domain Event
