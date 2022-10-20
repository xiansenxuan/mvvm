package com.demo.lib_base.http;

/**
 * com.hhd.workman.rx.http
 *
 * @author by Administrator on 2019/3/8 0008
 * @version [版本号, 2019/3/8 0008]
 * @update by Administrator on 2019/3/8 0008
 * @descript 网络相关属性配置
 */
public class ServicePropertyInter {
    public static  final Object[] baseUrls={
            "http://zhihuigongjiang.com:8787/", // 正式服务器 0（修改URL时带最后一个反斜杠）
            "http://test.zhihuigongjiang.com:8787/", // 测试服务器 1
            "http://ai.zhihuigongjiang.com:8585/", // 演示环境 2
            "http://test2.zhihuigongjiang.com:8585/", // 期测试环境 3
            "http://192.168.0.19:8585/", // 谢鹏服务器 4
            "http://192.168.0.20:8585/", // 余杭服务器 5
            "http://192.168.0.30:8585/", // 陈林服务器 6
            "http://192.168.0.28:8585/", // 吴开全服务器 7
            "http://192.168.0.52:8585/", // 唐文池服务器 8
            "http://192.168.0.13:8585/", // 杜恒服务器 9
            "http://192.168.0.107:8585/", // 宋子豪服务器 10
            "http://192.168.0.46:8585/", // 董超力服务 11

    };

    public static  final Object[] webBaseUrls={
            "http://www.zhihuigongjiang.com:8081/", // 正式服务器 0
            "http://test.zhihuigongjiang.com:8081/", // 测试服务器 1
            "http://ai.zhihuigongjiang.com:8081/", // 演示环境 2
            "http://test2.zhihuigongjiang.com:8081/" // 测试环境 3
    };

    /**
     * 手动修改 baseUrls webBaseUrls 0-11
     */
    public static  final int manualBaseIndex=3;

    public static  String BASE_URL=baseUrls[manualBaseIndex].toString(); // 基本请求

    public static  String web_url =
            webBaseUrls[manualBaseIndex>=webBaseUrls.length?4:manualBaseIndex].toString(); // web页

    public static  final String FACE_URL = "https://aip.baidubce.com/";

    //h5静态页链接
    public static final String staticWebUrl="https://www.zhihuigongjiang.com/static/";
    //public static final String staticWebUrl="https://ai.zhihuigongjiang.com/static/";
    //public static final String staticWebUrl=" https://test2.zhihuigongjiang.com/static/";

    //连接超时
    public static  final int default_timeout = 15*1000;
    //读写超时
    public static  final int write_timeout = 300*1000;
    public static  final int read_timeout = 300*1000;

    public static  final String login_token = "token";
    public static  final String device_info = "deviceInfo";
}
