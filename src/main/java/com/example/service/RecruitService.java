package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.role.Recruit;

import java.util.List;

public interface RecruitService extends IService<Recruit> {
    Recruit getRecruit(int askerId);
    int closeRecruit(int recruitId);
    IPage<Recruit> getRecruitList(Page<Recruit> page, LambdaQueryWrapper<Recruit> queryWrapper);
}
