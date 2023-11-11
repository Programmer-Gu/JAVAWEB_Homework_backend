package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.role.Recruit;

import java.util.List;

public interface RecruitService extends IService<Recruit> {
    Recruit getRecruit(int askerId);
    int closeRecruit(int recruitId);
    List<Recruit> getRecruitList();
}
