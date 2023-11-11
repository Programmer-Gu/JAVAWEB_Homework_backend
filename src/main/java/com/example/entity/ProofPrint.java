package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProofPrint {
    private int printID;
    private int employeeID;
    private Date printDate;
    private String documentType;
}
