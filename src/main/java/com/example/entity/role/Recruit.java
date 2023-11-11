package com.example.entity.role;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Recruit implements Serializable {
    private int recruitId;
    private int askerId;
    private String email;
    private String name;
    private String content;
    private int state = -1;
    private Timestamp sendTime;
}
