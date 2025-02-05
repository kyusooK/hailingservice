package hailingservice.domain;

import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class Operated extends AbstractEvent {

    private Long id;
    private String passengerLocation;
    private String destination;
    private DriverId driverId;
    private UserId userId;
    private OperationStatus operationStatus;
}
