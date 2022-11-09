package com.demo.lib_base.inter;

import android.os.Environment;

import java.io.File;

/**
 * com.hhd.workman.inter
 *
 * @author by xuan on 2018/7/4
 * @version [版本号, 2018/7/4]
 * @update by xuan on 2018/7/4
 * @descript 默认配置文件
 */
public interface SystemDefaultConfig {
    boolean isDebug=true;
    boolean isSupportModule=false;

    String current_style_mode = "current_style_mode";

    int sequence=2212;

    String LOG_TAG="MyLogger";
    String LIFE_TAG = "LIFE_TAG";
    String RX_TAG = "RX_TAG";
    String BAIDU_TAG    = "BAIDU_TAG";

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator+"zhgj"+File.separator;
    String cachePath = path + File.separator+"cache"+File.separator;
    String downloadPath = path +File.separator+ "download"+File.separator;
    String logPath = path +File.separator+ "log"+File.separator;
    String cameraPath = path +File.separator+ "camera"+File.separator;
    String videoPath = path +File.separator+ "video"+File.separator;
    String audioPath = path +File.separator+ "audio"+File.separator;
    String compressImagePath = path +File.separator+ "compress"+File.separator;

    int page_size = 20;

    /**
     * 保存sp的配置文件名
     */
    String shared_path = "config";

    String fragment_content="content";


    /**
     * 连续点击时间
     */
    int quick_click_time = 700;
}
