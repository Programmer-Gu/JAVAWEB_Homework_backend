package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Article;
import com.example.entity.role.Recruit;
import com.example.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/article")
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles/{pageNumber}")
    public Result<Object> getAllArticle(@PathVariable int pageNumber){

        Page<Article> page = new Page<>(pageNumber,10);
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = Wrappers.lambdaQuery(Article.class);
        articleLambdaQueryWrapper.select(Article::getArticleId,Article::getArticleType,Article::getTitle,Article::getDateTime);
        IPage<Article> articleList = articleService.getAllArticle(page,articleLambdaQueryWrapper);

        if(articleList != null){
            return Result.success("获取文章信息成功！", articleList);
        }
        return Result.error("获取文章信息失败");
    }

    @GetMapping("/articleInfo")
    public Result<Object> getArticleInfo( @RequestParam int articleId ){
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getArticleId, articleId);
        Article getArticle = articleService.getOne(lambdaQueryWrapper);

        if(getArticle != null){
            return Result.success("获取文章内容成功！", getArticle);
        }
        return Result.error("获取文章信息失败");
    }
}
