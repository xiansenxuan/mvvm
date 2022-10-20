package com.demo.lib_base.utils;

import com.tencent.mmkv.MMKV;

/**
 * Created by xuan on 2017/5/19.
 */

public class SharedPreferencesUtils {

    public static void putInt(String key, int defValue) {
       MMKV.defaultMMKV().encode(key,defValue);
    }

    public static int getInt(String key, int defValue) {
        return MMKV.defaultMMKV().decodeInt(key,defValue);
    }

    public static void putLong(String key, long value) {
       MMKV.defaultMMKV().encode(key,value);
    }

    public static long getLong(String key, long defValue) {
        return MMKV.defaultMMKV().decodeLong(key,defValue);
    }

    public static void putString(String key, String value) {
       MMKV.defaultMMKV().encode(key,value==null?"":value);
    }

    public static String getString(String key) {
        return MMKV.defaultMMKV().decodeString(key,"");
    }

    public static void putBoolean(String key, boolean value) {
       MMKV.defaultMMKV().encode(key,value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return MMKV.defaultMMKV().decodeBool(key,defValue);
    }
}
