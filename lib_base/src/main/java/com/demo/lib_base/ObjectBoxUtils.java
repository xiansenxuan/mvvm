package com.demo.lib_base;

import com.demo.entity.home.MyObjectBox;
import com.demo.lib_base.app.MyApplication;

import io.objectbox.BoxStore;


/**
 * @author: wanglei
 * @Description: com.demo.lib_base
 * @since: 2021/3/4 16:21
 */
public class ObjectBoxUtils {
    private ObjectBoxUtils() {
    }

    public static BoxStore getBoxStore() {
        return ObjectBoxHolder.BOX_STORE_BUILDER;
    }

    private static class ObjectBoxHolder {
        private static final BoxStore BOX_STORE_BUILDER = MyObjectBox.builder().androidContext(MyApplication.getInstance()).build();
    }
}
