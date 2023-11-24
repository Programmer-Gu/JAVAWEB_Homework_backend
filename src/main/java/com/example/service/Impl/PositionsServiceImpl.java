package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Positions;
import com.example.mapper.PositionsMapper;
import com.example.mapper.PositionsMapper.PositionMapper;
import com.example.service.PositionsService;
import org.springframework.stereotype.Service;

@Service
public class PositionsServiceImpl extends ServiceImpl<PositionMapper, Positions> implements PositionsService {

}
