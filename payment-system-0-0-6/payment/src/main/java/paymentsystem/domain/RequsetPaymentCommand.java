package paymentsystem.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RequsetPaymentCommand {

    private Long id;
    private String paymethod;
    private String storeId;
    private Integer price;
    private Date timeStamp;
    private String name;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
}
