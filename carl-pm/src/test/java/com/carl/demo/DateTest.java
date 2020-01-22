package com.carl.demo;

import com.carl.demo.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {

    public static void main(String[] args) {
        Date curDate = new Date();
        Date nextDate = DateUtil.addDays(curDate,1);
        long a = DateUtil.getDaysBetweenIgnoreTime(curDate,nextDate);
        System.out.println(curDate);
        System.out.println(nextDate);
        System.out.println(a);
    }
}
