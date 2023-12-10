package com.example.entity.selectEntity;

import com.example.entity.AcademicTitle;
import com.example.entity.Department;
import com.example.entity.Positions;
import com.example.entity.role.Employee;
import lombok.Data;

@Data
public class DetailedEmployeeInfo {
    private String realName;
    private Employee employee;
    private Department department;
    private Positions positions;
    private AcademicTitle academicTitle;
}
