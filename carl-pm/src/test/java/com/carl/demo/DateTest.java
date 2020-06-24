package com.carl.demo;

import com.carl.demo.util.DateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateTest {

    public static void main(String[] args) {
//        Date curDate = new Date();
//        Date nextDate = DateUtil.addDays(curDate,1);
//        long a = DateUtil.getDaysBetweenIgnoreTime(curDate,nextDate);
//        System.out.println(curDate);
//        System.out.println(nextDate);
//        System.out.println(a);

        BigDecimal bigDecimal = new BigDecimal(194140000l);
        BigDecimal bigDecimal1 = new BigDecimal(99998269999l);
        BigDecimal bigDecimal2 = new BigDecimal(18250000l);
        BigDecimal bigDecimal3 = new BigDecimal(20000000l);
        BigDecimal bigDecimal4 = new BigDecimal(20000000000l);
        BigDecimal bigDecimal5 = new BigDecimal(12359997155793.01d);
        BigDecimal bigDecimal6 = new BigDecimal(12357397155793.01d);
        System.out.println(bigDecimal.add(bigDecimal1).add(bigDecimal2).add(bigDecimal3).add(bigDecimal4));
        System.out.println(bigDecimal5.subtract(bigDecimal6));


    }
}
