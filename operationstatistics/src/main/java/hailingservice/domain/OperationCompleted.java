package hailingservice.domain;

import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class OperationCompleted extends AbstractEvent {

    private Long id;
    private Object driverId;
    private String destination;
    private Object operationStatus;
}
