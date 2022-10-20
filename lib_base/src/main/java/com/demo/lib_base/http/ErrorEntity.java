package com.demo.lib_base.http;

/**
 * com.hhd.workman.rx.http
 *
 * @author by xuan on 2018/9/13
 * @version [版本号, 2018/9/13]
 * @update by xuan on 2018/9/13
 * @descript
 */
public class ErrorEntity{
    public int errorCode;
    public String errorMsg;

    public ErrorEntity(int errorCode, String errorMsg) {
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }
}
