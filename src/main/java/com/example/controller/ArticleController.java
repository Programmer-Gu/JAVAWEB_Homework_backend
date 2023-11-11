package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.entity.Article;
import com.example.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/article")
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public Result<Object> getAllArticle(){
        List<Map<String, Object>> articleList = articleService.getAllArticle();

        if(articleList != null){
            return Result.success("获取全部文章信息成功！", articleList);
        }
        return Result.error("获取文章信息失败");
    }

    @GetMapping("/articleInfo")
    public Result<Object> getArticleInfo( int articleId ){
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getArticleId, articleId);
        Article getArticle = articleService.getOne(lambdaQueryWrapper);

        if(getArticle != null){
            return Result.success("获取文章内容成功！", getArticle);
        }
        return Result.error("获取文章信息失败");
    }
}
