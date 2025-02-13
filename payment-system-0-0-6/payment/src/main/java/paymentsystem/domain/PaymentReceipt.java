package paymentsystem.domain;

import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "PaymentReceipt_table")
@Data
public class PaymentReceipt {

    @Id
    private Long id;
}
