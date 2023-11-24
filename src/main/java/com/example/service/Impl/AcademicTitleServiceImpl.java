package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.AcademicTitle;
import com.example.mapper.AcademicTitleMapper;
import com.example.service.AcademicTitleService;
import org.springframework.stereotype.Service;

@Service
public class AcademicTitleServiceImpl extends ServiceImpl<AcademicTitleMapper, AcademicTitle> implements AcademicTitleService {
}
