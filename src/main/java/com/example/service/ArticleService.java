package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {
    public List<Map<String, Object>> getAllArticle();
}
