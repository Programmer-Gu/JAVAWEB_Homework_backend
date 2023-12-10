package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.role.Employee;
import com.example.entity.selectEntity.ConsultData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    @Select("Select COUNT(*) from employee inner join user on employee.user_id = user.user_id where user.birth_date is not null and user.birth_date >= #{startDate} and user.birth_date <= #{endDate}")
    long ageRangeCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Select("Select COUNT(*) from employee inner join user on employee.user_id = user.user_id where user.gender is not null and user.gender = #{sex}")
    long sexCount(@Param("sex") String sex);
}
