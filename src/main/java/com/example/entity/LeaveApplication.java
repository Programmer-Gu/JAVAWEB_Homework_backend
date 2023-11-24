package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LeaveApplication {
    private int applicationId;
    private int employeeId;
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;
}
