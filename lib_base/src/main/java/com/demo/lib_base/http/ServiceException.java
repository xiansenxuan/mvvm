package com.demo.lib_base.http;

/**
 * com.hhd.workman.rx.http
 *
 * @author by Administrator on 2019/3/7 0007
 * @version [版本号, 2019/3/7 0007]
 * @update by Administrator on 2019/3/7 0007
 * @descript
 */
public class ServiceException extends RuntimeException {
    public String message;
    public int code;

    public ServiceException(){
        super();
    }

    public ServiceException(String message,int code){
        super(message);

        this.message=message;
        this.code=code;
    }

    public ServiceException(String message,Throwable cause){
        super(message,cause);
    }


    public ServiceException(String message,Throwable cause,int code){
        super(message,cause);

        this.code=code;
    }
}
