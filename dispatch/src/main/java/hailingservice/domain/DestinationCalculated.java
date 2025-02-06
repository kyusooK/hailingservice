package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DestinationCalculated extends AbstractEvent {

    private Long id;
    private String passengerLocation;
    private String destination;
    private DriverId driverId;
    private Integer estimatedTime;
    private Integer estimatedDistance;

    public DestinationCalculated(Matching aggregate) {
        super(aggregate);
    }

    public DestinationCalculated() {
        super();
    }
}
//>>> DDD / Domain Event
