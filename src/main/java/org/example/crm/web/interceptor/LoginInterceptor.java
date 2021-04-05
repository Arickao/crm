package org.example.crm.web.interceptor;

import org.example.crm.setting.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 对异常的请求进行拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null){
            return false;
        }
        User obj = (User) session.getAttribute("user");
        if (obj == null){
            // 未登录，重定向到登陆页面
            response.sendRedirect(request.getContextPath() + "/welcome/login.jsp");
            return false;
        }
        return true;
    }
}
