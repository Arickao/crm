package org.example.crm.setting.service;

import org.example.crm.exception.LoginException;
import org.example.crm.setting.domain.User;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

public interface UserService {

    /**
     *
     * 判断用户登陆
     *
     * @param loginAct 用户输入的账号
     * @param loginPwd 用户输入的密码
     * @param ip 前端访问的ip
     * @return 存在返回一个User对象，不存在返回null
     * @throws LoginException 登陆错误异常
     */
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    /**
     * 在库中查找所有用户信息
     *
     * @return 所有的用户的id和名称
     */
    List<User> getAllUserIdAndName();

}
