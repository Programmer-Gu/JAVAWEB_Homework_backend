package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Consult;
import com.example.entity.selectEntity.ConsultData;
import com.example.mapper.ConsultTableMapper;
import com.example.mapper.UserMapper;
import com.example.service.ConsultTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultTableServiceImpl extends ServiceImpl<ConsultTableMapper, Consult> implements ConsultTableService {
    @Autowired
    private ConsultTableMapper consultTableMapper;

    @Override
    public IPage<ConsultData> getAllConsult(Page<ConsultData> page){
        return consultTableMapper.getConsultList(page);
    }
}
