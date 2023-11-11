package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Consult;
import com.example.mapper.ConsultTableMapper;
import com.example.service.ConsultTableService;
import org.springframework.stereotype.Service;

@Service
public class ConsultTableServiceImpl extends ServiceImpl<ConsultTableMapper, Consult> implements ConsultTableService {
}
