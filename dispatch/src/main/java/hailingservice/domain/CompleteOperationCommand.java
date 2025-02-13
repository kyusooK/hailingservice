package hailingservice.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class CompleteOperationCommand {

    private OperationStatus operationStatus;
    private Long fee;
}
