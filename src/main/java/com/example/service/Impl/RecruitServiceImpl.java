package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.role.Recruit;
import com.example.mapper.RecruitMapper;
import com.example.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitServiceImpl extends ServiceImpl<RecruitMapper, Recruit> implements RecruitService {
    @Autowired
    private RecruitMapper recruitMapper;

    public Recruit getRecruit(int askerId){
        return recruitMapper.getRecruit(askerId);
    }
    public int closeRecruit(int recruitId){
        return recruitMapper.closeRecruit(recruitId);
    }

    public List<Recruit> getRecruitList(){
        LambdaQueryWrapper<Recruit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Recruit::getName, Recruit::getEmail, Recruit::getSendTime ,Recruit::getState);
        return recruitMapper.selectList(lambdaQueryWrapper);
    }
}
