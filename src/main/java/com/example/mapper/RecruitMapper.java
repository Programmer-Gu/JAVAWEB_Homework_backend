package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.role.Recruit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RecruitMapper extends BaseMapper<Recruit> {
    @Select("SELECT * FROM recruit where asker_id = #{askerId} and state > -1 ORDER BY ABS(TIMESTAMPDIFF(SECOND, send_time, NOW())) LIMIT 1")
    Recruit getRecruit(@Param("askerId") int askerId);

    @Update("update recruit set state = -1 where recruit_id = #{recruitId}")
    int closeRecruit(@Param("recruitId") int recruitId);
}
