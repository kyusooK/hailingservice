package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GpsBasedLocationConfirmed extends AbstractEvent {

    private Long id;
    private String destination;
    private String passengerLocation;
    private DriverId driverId;
    private Integer estimatedTime;
    private Integer estimatedDistance;

    public GpsBasedLocationConfirmed(Matching aggregate) {
        super(aggregate);
    }

    public GpsBasedLocationConfirmed() {
        super();
    }
}
//>>> DDD / Domain Event
