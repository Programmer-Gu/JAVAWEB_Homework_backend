package com.example.utils;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class GenerateWrapper {
    public static <T> UpdateWrapper<T> getUpdateWrapper(T data, String... excludeFields){
        if (data == null) {
            return null; // 或者抛出异常
        }

        Set<String> excludeFieldSet = new HashSet<>(Arrays.asList(excludeFields));
        excludeFieldSet.add("class");
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        // 获取 data 对象的类
        Class<?> clazz = data.getClass();
        // 遍历类中的所有方法
        for (Method method : clazz.getMethods()) {
            // 检查方法名称是否以 "get" 开头并且无参数
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                try {
                    // 调用 get 方法并获取返回值
                    Object value = method.invoke(data);
                    if( value != null ){
                        // 获取方法名并转换为数据库字段风格
                        String dbFieldName = toDatabaseFieldName(method.getName());
                        // 将结果放入 resultMap
                        if( !excludeFieldSet.contains(dbFieldName)){
                            updateWrapper.set(dbFieldName, value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return updateWrapper;
    }

    // 将驼峰命名转换为下划线命名
    private static String toDatabaseFieldName(String methodName) {
        // 移除 "get" 并将首字母转为小写
        String field = methodName.substring(3);
        field = Character.toLowerCase(field.charAt(0)) + field.substring(1);

        // 将大写字母转换为 "_小写"
        StringBuilder dbFieldName = new StringBuilder();
        for (char ch : field.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                dbFieldName.append('_').append(Character.toLowerCase(ch));
            } else {
                dbFieldName.append(ch);
            }
        }
        return dbFieldName.toString();
    }

}
