package org.example.crm.setting.web.controller;

import org.example.crm.exception.LoginException;
import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;



@Controller
@RequestMapping("/setting")
public class UserController {

    @Resource
    private UserService userService;

    /**
     *
     * 处理登录请求
     *
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request,String account, String password){
        Map<String,Object> map = new HashMap<>(16);

        // 将密码的明文形式转换为MD5的密文形式
        password = MD5Util.getMD5(password);
        // 接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        try {
            User user = userService.login(account,password,ip);
            request.getSession().setAttribute("user",user);
            map.put("success",true);
            return map;
        } catch (LoginException loginException) {
            loginException.printStackTrace();
            map.put("success",false);
            map.put("msg",loginException.getMessage());
        } finally {
            return  map;
        }

    }


}
