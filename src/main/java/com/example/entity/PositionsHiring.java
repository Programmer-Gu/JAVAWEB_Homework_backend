package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PositionsHiring {
    private int hiringID;
    private int employeeID;
    private int positionsID;
    private Date hiringDate;
    private Date contractEndDate;
    private Boolean haveInstitutionalStaff;
}
