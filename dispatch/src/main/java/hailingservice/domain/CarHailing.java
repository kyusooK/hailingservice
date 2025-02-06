package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CarHailing extends AbstractEvent {

    private Long id;
    private String passengerLocation;
    private UserId userId;
    private String destination;

    public CarHailing(Operation aggregate) {
        super(aggregate);
    }

    public CarHailing() {
        super();
    }
}
//>>> DDD / Domain Event
