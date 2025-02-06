package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class DestinationCalculated extends AbstractEvent {

    private Long id;
    private String passengerLocation;
    private String destination;
    private Object driverId;
    private Integer estimatedTime;
    private Integer estimatedDistance;
}
