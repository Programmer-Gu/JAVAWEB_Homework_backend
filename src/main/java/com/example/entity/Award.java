package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Award {
    private int awardID;
    private int employeeID;
    private String awardName;
    private Date awardDate;
    private String issuedBy;
}
