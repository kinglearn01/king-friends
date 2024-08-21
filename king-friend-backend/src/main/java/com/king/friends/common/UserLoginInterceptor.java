package com.king.friends.common;

import com.king.friends.model.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.king.friends.contant.UserConstant.USER_LOGIN_STATE;

public class UserLoginInterceptor implements HandlerInterceptor {

    /***
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(USER_LOGIN_STATE);
            if (user != null) {
                return true;
            }
            //重定向登录页面
            response.sendRedirect(request.getContextPath() + "/user/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }


}



