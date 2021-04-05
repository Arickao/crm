package org.example.crm.setting.dao;

import org.example.crm.setting.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    /**
     *
     * 根据账号与密码查找
     *
     * @param map loginAct,loginPwd
     * @return 返回User
     */
    User login(Map<String, String> map);


    /**
     *
     * 查询所有的用户信息
     *
     * @return 所有的用户的id和名称
     */
    List<User> getAllUserIdAndName();
}
