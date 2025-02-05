package hailingservice.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "OperationRecord_table")
@Data
public class OperationRecord {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String driverId;
    private String userId;
    private String startingPoint;
    private String destination;
}
