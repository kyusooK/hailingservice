package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DriverDisapproved extends AbstractEvent {

    private Long id;
    private Boolean isApproved;

    public DriverDisapproved(Driver aggregate) {
        super(aggregate);
    }

    public DriverDisapproved() {
        super();
    }
}
//>>> DDD / Domain Event
