package com.demo.lib_base.constant;

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

    public static final String FILE_SEP = System.getProperty("file.separator");

    String LOG_TAG="MyLogger";
    String LIFE_TAG = "LIFE_TAG";
    String RX_TAG = "RX_TAG";

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator+"galaxis"+File.separator;
    String cachePath = path + File.separator+"cache"+File.separator;
    String downloadPath = path +File.separator+ "download"+File.separator;
    String logPath = path +File.separator+ "log"+File.separator;
    String cameraPath = path +File.separator+ "camera"+File.separator;
    String videoPath = path +File.separator+ "video"+File.separator;
    String audioPath = path +File.separator+ "audio"+File.separator;
    String compressImagePath = path +File.separator+ "compress"+File.separator;

    /**
     * 保存sp的配置文件名
     */
    String shared_path = "config";

    String fragment_content="content";

    /**
     * 连续点击时间
     */
    int quick_click_time = 700;
    int pageSize = 20;
}
