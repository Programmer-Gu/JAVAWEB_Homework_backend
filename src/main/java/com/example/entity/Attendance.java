package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Attendance {
    private int attendanceID;
    private int employeeID;
    private Date date;
    private String status;
}
