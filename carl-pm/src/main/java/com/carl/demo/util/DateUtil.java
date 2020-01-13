package com.carl.demo.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String transferDateFormat(String oldDateStr) {
        if (StringUtils.isBlank(oldDateStr)) {
            return null;
        }
        String newStr = null;
        Date date;
        String dateStr;
        try {
            dateStr = oldDateStr.replace("Z", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            date = sdf.parse(dateStr);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            newStr = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newStr;
    }

}
