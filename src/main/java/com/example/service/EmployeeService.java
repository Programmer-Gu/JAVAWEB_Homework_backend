package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.requestDataForm.EmployeeGraph;
import com.example.entity.requestDataForm.SexData;
import com.example.entity.role.Employee;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    EmployeeGraph getEmpGraphData();
}
