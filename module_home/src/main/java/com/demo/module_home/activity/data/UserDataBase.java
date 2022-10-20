package com.demo.module_home.activity.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.data
 * @since: 2021/3/2 17:55
 */
@Database(entities = UserData.class,version = 1,exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {

    public abstract UserDao getUserDao();

    private static UserDataBase instance;

    public static UserDataBase getInstance(Context context){
        if (instance == null){
            synchronized (UserDataBase.class){
                if (instance == null){
                    instance = create(context);
                }
            }
        }
        return instance;
    }

    private static UserDataBase create(Context context) {
        return Room.databaseBuilder( context,UserDataBase.class,"user_db").allowMainThreadQueries().build();
    }
}
