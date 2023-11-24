package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Article;
import com.example.entity.role.Recruit;
import com.example.mapper.ArticleMapper;
import com.example.mapper.UserMapper;
import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public IPage<Article> getAllArticle(Page<Article> page, LambdaQueryWrapper<Article> queryWrapper){
        return articleMapper.selectPage(page,queryWrapper);
    }
}
