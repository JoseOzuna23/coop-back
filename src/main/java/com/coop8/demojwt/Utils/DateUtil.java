package com.coop8.demojwt.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatoFecha(String pattern, Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}

