package com.demo.lib_base.clash;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * com.zhgj.app.base.crash
 *
 * @author by Administrator on 2020/6/19 0019
 * @version [版本号, 2020/6/19 0019]
 * @update by Administrator on 2020/6/19 0019
 * @descript
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    public static final String ERROR_INFORMATION = "ERROR_INFORMATION";

    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        Intent intent=new Intent(mContext,MyCrashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //获取异常数据
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String errorInformation = sw.toString();

        if (errorInformation.length() > MAX_STACK_TRACE_SIZE) {
            String disclaimer = " [stack trace too large]";
            errorInformation = errorInformation.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
        }

        intent.putExtra(ERROR_INFORMATION,errorInformation);
        mContext.startActivity(intent);
/*

        if(!SystemDefaultConfig.isDebug){
            //保存数据库
            DaoSession daoSession = MyApplication.getInstance().getDaoSession();
            CrashInfoDataDao crashInfoDataDao=daoSession.getCrashInfoDataDao();

            CrashInfoData data=new CrashInfoData();
            data.type="android";
            data.username= LoginUtils.getAccount();
            data.phoneModel= SystemServiceUtil.getDeviceBrand()+SystemServiceUtil.getSystemModel();

            String versionName = "";
            try {
                versionName = mContext.getPackageManager().
                        getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            data.appVersion= versionName;
            data.warnTime= DateTime.now().toString(DateTimeUtils.YYYY_MM_DD_HH_MM_SS);
            //data.errorInformation=errorInformation;

            crashInfoDataDao.insert(data);
        } */



        return true;
    }
}