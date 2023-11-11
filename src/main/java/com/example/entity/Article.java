package com.example.entity;
import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private int articleId;
    private String title;
    private String type;
    private Date DateTime;
    private int userId;
    private String content;
}
