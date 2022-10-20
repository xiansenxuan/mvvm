package com.demo.lib_base.entity.entity;

/**
 * com.hhd.workman.rx.http
 *
 * @author by Administrator on 2019/3/6 0006
 * @version [版本号, 2019/3/6 0006]
 * @update by Administrator on 2019/3/6 0006
 * @descript
 */
public class SupportBaseEntity<T> {
    public int code;
    public String msg;
    public T data;
    public boolean success;

}
