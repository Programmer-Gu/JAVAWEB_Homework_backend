package com.example.entity;

import lombok.Data;

@Data
public class Certification {
    private int certificationID;
    private int employeeID;
    private String certificationName;
    private String issuedBy;
    private String validityDate;
}
