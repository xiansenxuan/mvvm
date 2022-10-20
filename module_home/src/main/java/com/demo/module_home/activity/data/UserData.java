package com.demo.module_home.activity.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author: wanglei
 * @Description: com.demo.module_home.activity.data
 * @since: 2021/3/2 17:17
 */
@Entity(tableName = "tb_user")
public class UserData {
    @PrimaryKey
    public int uid;

    public String userName;
    public int age;
    public int sex;
}
