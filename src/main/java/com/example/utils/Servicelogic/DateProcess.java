package com.example.utils.Servicelogic;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.Attendance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateProcess {
    public static Date getAgeYear( int age ){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTime();
    }
    public static List<Date> getAllDatesInMonth(int year, int month) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // 设置年份和月份
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // 注意：月份从0开始（0代表一月）
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 将日期设为当月第一天

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1); // 增加一天
        }
        return dates;
    }

    public static void setOneDayRange(AbstractLambdaWrapper<Attendance,?> lambdaWrapper, Date specifyTime){
        Calendar calendar = Calendar.getInstance();
        if(specifyTime != null){
            calendar.setTime(specifyTime);
        }
        // 设置时间为当天的开始（午夜）
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        // 设置时间为当天结束（午夜前的一刻）
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();
        lambdaWrapper.between(Attendance::getDate, startDate, endDate);
    }

    public static Date getOneDayDate( int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        // 设置年份和月份
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // 注意：月份从0开始（0代表一月）
        calendar.set(Calendar.DAY_OF_MONTH, day); // 将日期设为当月第一天
        return calendar.getTime();
    }
}
