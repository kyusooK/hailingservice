package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class GpsBasedLocationConfirmed extends AbstractEvent {

    private Long id;
    private String destination;
    private String passengerLocation;
    private Integer estimatedTime;
    private Integer estimatedDistance;
    private Object userId;
}
