package com.demo.entity.home;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author: wanglei
 * @Description: com.demo.entity.home
 * @since: 2021/3/4 17:27
 */
@Entity
public class UserEntity {
    @Id
    public long uid;

    public String userName;

    public int age;

    public int sex;
}
