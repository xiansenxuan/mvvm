package com.demo.module_home.activity.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.data
 * @since: 2021/3/3 15:53
 */
@Dao
public interface UserDao {
    @Query("select * from tb_user")
    ArrayList<LiveData<UserData>> query();

    @Query("select * from tb_user where uid=:uid")
    LiveData<UserData> queryByUid(int uid);

    @Insert()
    void insert(UserData userData);

    @Update()
    void update(UserData userData);

    @Delete()
    void delete(UserData userData);
}
