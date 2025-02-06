package hailingservice.domain;

import hailingservice.domain.*;
import hailingservice.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class HailingAccepted extends AbstractEvent {

    private Long id;
    private Boolean isHailing;
    private String driverLocation;
    private String operationRequestForm;
    private Long operationRequestId;
}
