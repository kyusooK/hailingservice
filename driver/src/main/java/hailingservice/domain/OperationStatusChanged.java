package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OperationStatusChanged extends AbstractEvent {

    private Long id;
    private Boolean isHailing;
    private String driverLocation;

    public OperationStatusChanged(Driver aggregate) {
        super(aggregate);
    }

    public OperationStatusChanged() {
        super();
    }
}
//>>> DDD / Domain Event
