package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
    @Select("Select COUNT(*) from employee inner join user on employee.user_id = user.user_id where user.gender is not null and user.gender = #{sex}")
    long sexCount(@Param("sex") String sex);
}
