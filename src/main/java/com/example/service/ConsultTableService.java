package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Consult;
import com.example.entity.selectEntity.ConsultData;

import java.util.List;

public interface ConsultTableService extends IService<Consult> {
    IPage<ConsultData> getAllConsult(Page<ConsultData> page);
}
