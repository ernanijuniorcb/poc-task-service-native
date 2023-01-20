package com.example.demo.domain;

import lombok.Data;

@Data
public class TaskEntity {

    String eid;
    String outletId;
    String assignedTo;
    String dueDateTime;
}

