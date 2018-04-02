package com.sumslack.opensource.weex.util;

/**
 * Created by sospartan on 5/31/16.
 */
public class AssertUtil {
    public static<T extends Exception> void throwIfNull(Object object,T e) throws T {
        if(object == null){
            throw e;
        }
    }
}