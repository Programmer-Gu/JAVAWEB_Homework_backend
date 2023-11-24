package com.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Consult implements Serializable {
    private int consultId;
    private int askerId;
    private String email;
    private String title;
    private String content;
    private int state;
    private Timestamp sendTime;
    private Timestamp replyTime;
}
