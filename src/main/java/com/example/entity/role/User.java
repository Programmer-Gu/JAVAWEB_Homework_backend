package com.example.entity.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private int userId;
    private String password;
    private int authority = 10;
    private String idNumber;
    private String userName;
    private String gender;
    private Date birthDate;
}
