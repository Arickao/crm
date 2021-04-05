package org.example.crm.setting.service.impl;

import org.example.crm.exception.LoginException;
import org.example.crm.setting.dao.UserDao;
import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>(16);
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("账号密码错误");
        }

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0 ){
            throw new LoginException("账号已失效");
        }

        // 判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号被锁定");
        }

        //判断ip
        /*String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }*/
        return user;

    }

    @Override
    public List<User> getAllUserIdAndName() {
        List<User> userList = userDao.getAllUserIdAndName();
        return userList;
    }

}
