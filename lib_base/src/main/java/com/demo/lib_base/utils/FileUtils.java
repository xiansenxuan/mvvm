package com.demo.lib_base.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * com.hhd.workman.utils
 *
 * @author by Administrator on 2019/6/13 0013
 * @version [版本号, 2019/6/13 0013]
 * @update by Administrator on 2019/6/13 0013
 * @descript
 */
public class FileUtils {
    /**
     * Delete file if exist file
     *
     * @param file the file
     * @return true if this file was deleted, false otherwise.
     */
    public static boolean deleteFileIfExist(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Delete file if exist file
     *
     * @param path
     * @return true if this file was deleted, false otherwise.
     */
    public static boolean deleteFileIfExist(String path) {
        if(TextUtils.isEmpty(path)){
            return false;
        }
        File file=new File(path);

        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean isValidFile(String path){
        if(TextUtils.isEmpty(path)){
            return false;
        }

        File file=new File(path);

        if (file == null) {
            return false;
        }

        if (!file.exists()) {
            return false;
        }

        return true;
    }

    public static boolean isValidFile(File file){
        if (file == null) {
            return false;
        }

        if (!file.exists()) {
            return false;
        }

        return true;
    }


    public static boolean isValidUrl(String url){
        if(!TextUtils.isEmpty(url) && url.startsWith("http")){
            return true;
        }

        return false;
    }
}
