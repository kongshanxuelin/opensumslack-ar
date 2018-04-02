package com.sumslack.opensource.utils;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class StrUtil {
    public static String formatMullStr(Object obj){
        if(obj == null) return "";
        if(obj.toString().equalsIgnoreCase("null")) return "";
        return obj.toString();
    }

}
