package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class HailingRejected extends AbstractEvent {

    private Long id;
    private Boolean isHailing;

    public HailingRejected(Driver aggregate) {
        super(aggregate);
    }

    public HailingRejected() {
        super();
    }
}
//>>> DDD / Domain Event
