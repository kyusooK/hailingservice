package hailingservice.external;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
public class RequsetPaymentCommand {

    @Id
    private Long id;

    private Long itemId;
    private Integer price;
    private String name;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
}
