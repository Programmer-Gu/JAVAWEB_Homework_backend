package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Positions;
import org.apache.ibatis.annotations.Mapper;

public interface PositionsMapper extends BaseMapper<Positions> {
    @Mapper
    interface PositionMapper extends BaseMapper<Positions> {
    }
}
