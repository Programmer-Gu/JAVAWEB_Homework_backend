package com.example.entity.requestDataForm;

import lombok.Data;

@Data
public class SexData {
    private String name;
    private long value;

    public SexData( String sex, long value){
           this.name = sex;
           this.value = value;
    }
}
