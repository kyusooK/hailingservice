package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ReceivePaymentStatusCommand {

    private Long id;
    private String status;
    private String reason;
    private String paymentId;
}
