package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DriverRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String driverLicenseNumber;
    private Boolean isApproved;

    public DriverRegistered(Driver aggregate) {
        super(aggregate);
    }

    public DriverRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
