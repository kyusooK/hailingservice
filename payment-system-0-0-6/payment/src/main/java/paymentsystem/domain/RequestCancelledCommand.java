package paymentsystem.domain;

import lombok.Data;

@Data
public class RequestCancelledCommand {

    private Long id;
    private Long itemId;
    private String paymentId;
    private Integer price;
    private String status;
    private String reason;
}
