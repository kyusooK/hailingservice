package hailingservice.external;

import java.util.Date;
import lombok.Data;

@Data
public class Payment {

    private Long id;
    private Long itemId;
    private String paymentId;
    private Integer price;
    private String name;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
    private String status;
    private String reason;
}
