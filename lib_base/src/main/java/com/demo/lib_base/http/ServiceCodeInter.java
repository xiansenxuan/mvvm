package com.demo.lib_base.http;

/**
 * com.hhd.workman.rx.http
 *
 * @author by xuan on 2018/9/13
 * @version [版本号, 2018/9/13]
 * @update by xuan on 2018/9/13
 * @descript
 */
public interface ServiceCodeInter {
    //唯一成功标识
    int success=1;

    //自定义异常代码
    int unknown_error_code=96; // 未知错误
    int data_cannot_process=97; // 有数据但是处理不了 说明数据返回格式有问题
    int data_null=98; // 数据源空

    //服务器异常代码
    int error=0; // 直接显示msg的异常

    //系统异常代码
    int time_out_code =99;// 连接超时
    int disconnecting_code =100;// 连接断开
    int json_syntax_code =101;// json解析错误
    int unsupported_operation_error_code =103;// 数据类型转换错误
    int lost_page_code =102;// http 404
    int unknown_service_code =103;// 未知的ip地址

}
