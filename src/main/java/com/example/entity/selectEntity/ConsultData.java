package com.example.entity.selectEntity;

import com.example.entity.Consult;
import lombok.Data;

import java.util.Date;

@Data
public class ConsultData{
    private int consultId;
    private String userName;
    private String email;
    private String title;
    private String content;
    private int state;
    private Date sendTime;
}
