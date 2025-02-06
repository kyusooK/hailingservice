package hailingservice.external;

import java.util.Date;
import lombok.Data;

@Data
public class Driver {

    private Long id;
    private String email;
    private String driverLicenseNumber;
    private Boolean isApproved;
    private Boolean isHailing;
    private String driverLocation;
    private String operationRequestForm;
    private String operationInfo;
}
