package hailingservice.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ConfirmLicenseCommand {

    private Boolean isApproved;
    private String driverLicenseNumber;
}
