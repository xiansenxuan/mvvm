package com.demo.lib_base.http;

/**
 * Created by xuan on 2018/7/31
 */

public class SeparationResultException extends RuntimeException {
    public static final int on_success=1;
    public static final int on_error=0;
    public static final int on_reset_login=11;

    private boolean isSuccess;
    private int resultCode;
    private String detailMessage;

    public SeparationResultException(String detailMessage) {
        super(detailMessage);
    }

    public SeparationResultException(boolean isSuccess,int resultCode, String detailMessage) {
        this.isSuccess=isSuccess;
        this.resultCode=resultCode;
        this.detailMessage=detailMessage;
    }

    public boolean getResultIsSuccess(){
        return isSuccess;
    }

    public int getResultCode(){
        return resultCode;
    }

    public String getResultMessage(){
        return detailMessage;
    }
}
