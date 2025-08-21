package com.niqdev.authclient.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, 
                               Authentication authentication) throws IOException, ServletException {
        
        // 記錄登出事件
        if (authentication != null) {
            System.out.println("用戶 " + authentication.getName() + " 已登出");
        }
        
        // 清除所有相關的 cookie
        String[] cookiesToDelete = {"JSESSIONID", "remember-me", "oauth2_auth_request"};
        for (String cookieName : cookiesToDelete) {
            var cookie = new jakarta.servlet.http.Cookie(cookieName, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        
        // 重定向到登出成功頁面
        response.sendRedirect("/logout-success");
    }
}
