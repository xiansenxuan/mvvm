/*
 * Copyright 2014-2017 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.lib_base.clash;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.demo.lib_base.R;
import com.demo.lib_base.route.RouteUtils;
import com.demo.lib_base.activity.BaseActivity;
import com.demo.lib_base.databinding.ActivityCustomErrorBinding;
import com.demo.lib_base.constant.SystemDefaultConfig;
import com.demo.lib_base.utils.LoggerUtils;
import com.demo.lib_base.utils.ToastUtils;


public class MyCrashActivity extends BaseActivity {
    protected ActivityCustomErrorBinding mBinding;
    private String errorInformation="";

    @Override
    public void setRxListener() {

    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_error;
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    @Override
    public void initView(Bundle savedInstanceState) {
        ActivityCustomErrorBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_custom_error);

        errorInformation=getIntent().getStringExtra(CrashHandler.ERROR_INFORMATION);
        LoggerUtils.e(errorInformation);
        Log.e(SystemDefaultConfig.LOG_TAG,errorInformation);

        mBinding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartApplicationWithIntent();
            }
        });

        mBinding.moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //We retrieve all the error data and show it
                AlertDialog dialog = new AlertDialog.Builder(MyCrashActivity.this)
                        .setTitle(R.string.error_details)
                        .setMessage(errorInformation)
                        .setPositiveButton(R.string.close_app, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeApplication();
                            }
                        })
                        .setNeutralButton(R.string.copy_to_clipboard,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyErrorToClipboard();
                                    }
                                })
                        .show();
                TextView textView = dialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_text_size));
                }
            }
        });

    }
/*
    @SuppressLint("CheckResult")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //IMPORTANT
        //The custom error activity in this sample is uglier than the default one and just
        //for demonstration purposes, please don't copy it to your project!
        //We recommend taking the original library's DefaultErrorActivity as a basis.
        //Of course, you are free to implement it as you wish in your application.

        //These four methods are available for you to use:
        //CustomActivityOnCrash.getStackTraceFromIntent(getIntent()): gets the stack trace as a string
        //CustomActivityOnCrash.getActivityLogFromIntent(getIntent()): gets the activity log as a string
        //CustomActivityOnCrash.getAllErrorDetailsFromIntent(context, getIntent()): returns all error details including stacktrace as a string
        //CustomActivityOnCrash.getConfigFromIntent(getIntent()): returns the config of the library when the error happened

        //Now, treat here the error as you wish. If you allow the user to restart or close the app,
        //don't forget to call the appropriate methods.
        //Otherwise, if you don't finish the activity, you will get the MyCrashActivity on the activity stack and it will be visible again under some circumstances.
        //Also, you will get multiprocess problems in API<17.

        /*String errorDetails=CustomActivityOnCrash.getStackTraceFromIntent(getIntent());

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        Button restartButton = findViewById(R.id.customactivityoncrash_error_activity_restart_button);

        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(MyCrashActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(MyCrashActivity.this, config);
                }
            });
        }

        Button moreInfoButton = findViewById(R.id.customactivityoncrash_error_activity_more_info_button);

        if(SystemDefaultConfig.isDebug){
            moreInfoButton.setVisibility(View.VISIBLE);
        }else{
            moreInfoButton.setVisibility(View.GONE);
        }

        if (config.isShowErrorDetails()) {
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //We retrieve all the error data and show it

                    AlertDialog dialog = new AlertDialog.Builder(MyCrashActivity.this)
                            .setTitle(R.string.error_details)
                            .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(MyCrashActivity.this, getIntent()))
                            .setPositiveButton(R.string.close_app, null)
                            .setNeutralButton(R.string.copy_to_clipboard,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                        }
                                    })
                            .show();
                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
                    }
                }
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }

        Integer defaultErrorActivityDrawableId = config.getErrorDrawable();
        ImageView errorImageView = findViewById(R.id.customactivityoncrash_error_activity_image);

        if (defaultErrorActivityDrawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), defaultErrorActivityDrawableId, getTheme()));
        }

        //保存到本地
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(MyCrashActivity.this, getIntent());

        DaoSession daoSession = MyApplication.getInstance().getDaoSession();
        CrashInfoDataDao crashInfoDataDao=daoSession.getCrashInfoDataDao();

        CrashInfoData data=new CrashInfoData();
        data.type="android";
        data.username= LoginUtils.getAccount();
        data.phoneModel= SystemServiceUtil.getDeviceBrand()+SystemServiceUtil.getSystemModel();

        String versionName = "";
        try {
            versionName = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        data.appVersion= versionName;
        data.warnTime= DateTime.now().toString(DateTimeUtils.YYYY_MM_DD_HH_MM_SS);
        data.errorInformation=errorInformation;

        crashInfoDataDao.insert(data);
    }
*/

    private void copyErrorToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.error_details), errorInformation);
            clipboard.setPrimaryClip(clip);

            ToastUtils.showDefaultToast(MyCrashActivity.this,R.string.copy_to_clipboard);
        }
    }


    /**
     * Closes the app.
     * If an event listener is provided, the close app event is invoked.
     * Must only be used from your error activity.
     */
    public void closeApplication() {
        MyCrashActivity.this.finish();
        killCurrentProcess();
    }


    public void restartApplicationWithIntent() {
        MyCrashActivity.this.finish();
        RouteUtils.startMain();
        killCurrentProcess();

/*        Intent intent = new Intent(MyCrashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            //If the class name has been set, we force it to simulate a Launcher launch.
            //If we don't do this, if you restart from the error activity, then press home,
            //and then launch the activity from the launcher, the main activity appears twice on the backstack.
            //This will most likely not have any detrimental effect because if you set the Intent component,
            //if will always be launched regardless of the actions specified here.
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }

        MyCrashActivity.this.finish();
        MyCrashActivity.this.startActivity(intent);
        killCurrentProcess();

        */
    }

    /**
     * INTERNAL method that kills the current process.
     * It is used after restarting or killing the app.
     */
    private void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}
