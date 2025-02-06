package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class DriverMatched extends AbstractEvent {

    private Long id;
    private Object driverId;
    private String passengerLocation;
    private Object userId;
    private Integer estimatedTime;
    private Integer estimatedDistance;
    private String destination;
    private String driverLocation;
}
