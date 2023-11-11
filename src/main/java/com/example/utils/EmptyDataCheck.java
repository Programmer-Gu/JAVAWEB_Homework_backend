package com.example.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmptyDataCheck {
    public static <T> boolean checkData(T data, String... checkParam) {
        if (data == null) {
            return false; // 或者抛出异常
        }

        Class<?> clazz = data.getClass();
        Set<String> checkDataSet = new HashSet<>(Arrays.asList(checkParam));

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            String field = methodName.substring(3);
            field = Character.toLowerCase(field.charAt(0)) + field.substring(1);

            if (!checkDataSet.contains(field) || !method.getName().startsWith("get")) {
                continue;
            }
            try {
                Object value = method.invoke(data);
                if(value == null){
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
