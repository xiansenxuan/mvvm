package com.demo.module_home.activity.viewmodel;

import android.app.Application;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.demo.entity.home.UserEntity;
import com.demo.lib_base.ObjectBoxUtils;
import com.demo.lib_base.config.base.BaseViewModel;

import io.objectbox.Box;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.viewmodel
 * @since: 2021/3/2 14:47
 */
public class LoginViewModel extends BaseViewModel {
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> userName=new ObservableField<>();
    public ObservableField<String> password=new ObservableField<>();

    public MutableLiveData<Boolean> isLogin=new MutableLiveData<>();

    public void login(){
        Box<UserEntity> userBox= ObjectBoxUtils.getBoxStore().boxFor(UserEntity.class);

        UserEntity entity=new UserEntity();
        entity.age=18;
        entity.sex=1;
        entity.userName="小王";
        userBox.put(entity);

/*        ObjectBoxLiveData<UserEntity> listLiveData = new ObjectBoxLiveData(userBox.query().equal(UserEntity_.uid, 0).build());
        if(listLiveData.getValue()!=null && listLiveData.getValue().size()>0){
            UserEntity data= listLiveData.getValue().get(0);
            System.out.println(data.userName+" "+data.sex+" "+data.age);
        }*/

        SystemClock.sleep(500);

        isLogin.setValue(true);

        Toast.makeText(getApplication(),"登录成功",Toast.LENGTH_SHORT).show();
    }
}
