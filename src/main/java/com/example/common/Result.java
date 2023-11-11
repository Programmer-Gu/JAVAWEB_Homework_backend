package com.example.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务器相应的数据都会封装成此对象
 *
 * @param <T>
 */
@Data
public class Result<T> {

    private Integer code; //状态码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> Result<T> success(String msg, T object) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.data = object;
        result.code = 1;
        return result;
    }


    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public static <T> Result<T> error(String msg ) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }


}
