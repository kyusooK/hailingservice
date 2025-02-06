package hailingservice.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ChangeOperationstatusCommand {

    private Boolean isHailing;
    private String driverLocation;
}
