package com.example.entity;

import lombok.Data;

@Data
public class Department {
    private int departmentID;
    private String departmentName;
    private int parentDepartmentID;
}
