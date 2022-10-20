package com.demo.lib_base.http;

/**
 * com.hhd.workman.rx.http
 *
 * @author by Administrator on 2019/3/8 0008
 * @version [版本号, 2019/3/8 0008]
 * @update by Administrator on 2019/3/8 0008
 * @descript 网络访问错误提示内容
 */
public interface ServiceMessageInter {
    String serviceError="服务器错误，请重试";

    String dataNullMessage="数据源空，请重试";

    String dataCannotProcessMessage="数据处理失败，请重试";

    String resetLoginMessage="请重新登录";

    String timeOutMessage="连接超时，请重试";

    String disconnectingMessage="网络断开，请重试";

    String lostPageMessage="页面丢失了，请重试";

    String unknownServiceMessage="未知地址，请重试";

    String unknownErrorMessage="未知错误，请重试";
}
