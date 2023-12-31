package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Article;
import com.example.entity.role.Recruit;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {
    public IPage<Article> getAllArticle(Page<Article> page, LambdaQueryWrapper<Article> queryWrapper);
}
