package com.yasinzhang.applock.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import androidx.room.TypeConverter;

public class AppStringTypeConverter {
    @TypeConverter
    public static List<String> stringToAppList(String str) {
        if(str == null)
            return new ArrayList<String>();

        str = str.trim();
        if(str.length() == 0)
            return new ArrayList<String>();

        String []list = str.split(" ");
        return Arrays.asList(list);
    }

    @TypeConverter
    public static String appListToString(List<String> list) {
        String listStr = "";
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            listStr += it.next();
            listStr += " ";
        }
        listStr = listStr.trim();
        return listStr;
    }
}
