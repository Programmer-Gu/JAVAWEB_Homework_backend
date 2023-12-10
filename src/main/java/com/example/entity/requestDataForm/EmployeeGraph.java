package com.example.entity.requestDataForm;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeGraph {
    private List<Long> age;
    private List<SexData> sex;
    private List<Long> degree;
    private List<Long> occupation;
}
