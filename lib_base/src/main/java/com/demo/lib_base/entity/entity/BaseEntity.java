package com.demo.lib_base.entity.entity;

/**
 * Created by xuan on 2017/6/2.
 */

public class BaseEntity<T>   {
    public int code;
    public String msg;
    public T data;
    public boolean success;
}
