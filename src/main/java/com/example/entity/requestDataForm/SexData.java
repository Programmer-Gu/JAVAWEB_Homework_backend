package com.example.entity.requestDataForm;

import lombok.Data;

@Data
public class SexData {
    private String sex;
    private long value;

    public SexData( String sex, long value){
           this.sex = sex;
           this.value = value;
    }
}
