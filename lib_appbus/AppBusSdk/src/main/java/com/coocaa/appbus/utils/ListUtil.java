package com.coocaa.appbus.utils;

import java.util.List;

public class ListUtil {
    public static<T> boolean isEmpty(List<T> list){
        return list == null || list.isEmpty();
    }
}
