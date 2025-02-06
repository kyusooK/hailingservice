package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class HailingAccepted extends AbstractEvent {

    private Long id;
    private Boolean isHailing;
    private String driverLocation;
    private String operationRequestForm;
    private Long operationRequestId;

    public HailingAccepted(Driver aggregate) {
        super(aggregate);
    }

    public HailingAccepted() {
        super();
    }
}
//>>> DDD / Domain Event
