package com.example.ezlawbackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String path = request.getRequestURI();
        if(path.contains("/api/auth/login") || path.contains("/api/auth/signup")){
            return true;
        }

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("email") == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

}