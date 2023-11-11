package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AcademicSocialPositions {
    private int positionsID;
    private int employeeID;
    private String positionsName;
    private Date startDate;
    private Date endDate;
}
