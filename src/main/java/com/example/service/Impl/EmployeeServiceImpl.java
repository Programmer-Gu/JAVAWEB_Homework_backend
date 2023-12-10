package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.requestDataForm.EmployeeGraph;
import com.example.entity.requestDataForm.SexData;
import com.example.entity.role.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.utils.Servicelogic.DateProcess.getAgeYear;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    private final List<Integer> ageRange = new ArrayList<>(Arrays.asList(18, 30, 40, 50, 60, 200));
    private final List<String> gender = new ArrayList<>(Arrays.asList("男", "女"));
    private final List<Integer> degrees = new ArrayList<>(Arrays.asList(1, 2, 3));
    private final List<Integer> occupations = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

    private List<Long> getAgeRangeCount() {
        List<Long> empAge = new ArrayList<>();
        for (int i = 1; i < ageRange.size(); i++) {
            empAge.add(employeeMapper.ageRangeCount(getAgeYear(ageRange.get(i)), getAgeYear(ageRange.get(i-1))));
        }
        return empAge;
    }

    private List<SexData> getSexCount() {
        List<SexData> sexData = new ArrayList<>();
        for (String sex : gender) {
            sexData.add(new SexData(sex, employeeMapper.sexCount(sex)));
        }
        return sexData;
    }

    private List<Long> getDegreeCount() {
        List<Long> degreeValue = new ArrayList<>();
        for (int degree : degrees) {
            LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = Wrappers.lambdaQuery(Employee.class);
            employeeLambdaQueryWrapper.eq(Employee::getAcademicTitleId, degree);
            degreeValue.add(employeeMapper.selectCount(employeeLambdaQueryWrapper));
        }
        return degreeValue;
    }

    private List<Long> getOccupationCount() {
        List<Long> occupationsValue = new ArrayList<>();
        for (int occupation : occupations) {
            LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = Wrappers.lambdaQuery(Employee.class);
            employeeLambdaQueryWrapper.eq(Employee::getPositionsId, occupation);
            occupationsValue.add(employeeMapper.selectCount(employeeLambdaQueryWrapper));
        }
        return occupationsValue;
    }

    public EmployeeGraph getEmpGraphData(){
        EmployeeGraph employeeGraph = new EmployeeGraph();
        employeeGraph.setAge(getAgeRangeCount());
        employeeGraph.setDegree(getDegreeCount());
        employeeGraph.setSex(getSexCount());
        employeeGraph.setOccupation(getOccupationCount());
        return employeeGraph;
    }
}
