package hailingservice.external;

import java.util.*;

import lombok.Data;

@Data
public class Reservation {

    private String taskId;
    private List<String> targetUserIds;
    private String title;
    private String description;
    private Date dueDate;
    private boolean now;
}
