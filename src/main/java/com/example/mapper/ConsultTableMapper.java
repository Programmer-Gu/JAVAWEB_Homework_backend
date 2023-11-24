package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Consult;
import com.example.entity.selectEntity.ConsultData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConsultTableMapper extends BaseMapper<Consult> {
    @Select("Select user_name,consult_id, email, title, content, state, send_time from consult inner join user on consult.asker_id = user.user_id where state = 0")
    IPage<ConsultData> getConsultList(Page<ConsultData> page);
}
