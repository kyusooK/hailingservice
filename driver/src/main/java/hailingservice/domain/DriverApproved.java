package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DriverApproved extends AbstractEvent {

    private Long id;
    private String email;
    private String driverLicenseNumber;
    private Boolean isApproved;
    private Boolean isHailing;
    private String driverLocation;

    public DriverApproved(Driver aggregate) {
        super(aggregate);
    }

    public DriverApproved() {
        super();
    }
}
//>>> DDD / Domain Event
