package com.niqdev.authclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {
    
    /**
     * 處理 OAuth2 授權取消錯誤
     * 當用戶在 consent 頁面點擊取消時，會跳轉到此頁面
     */
    @GetMapping("/auth-error")
    public String handleAuthError(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("access_denied".equals(error)) {
            // OAuth2 授權被取消
            model.addAttribute("errorType", "authorization_cancelled");
            model.addAttribute("errorTitle", "授權已取消");
            model.addAttribute("errorMessage", "您已取消授權流程，無法完成登入");
            model.addAttribute("errorDescription", "在授權頁面中，您選擇了取消授權。如果您想要登入，請重新開始授權流程。");
            model.addAttribute("errorCode", "ACCESS_DENIED");
            return "auth-error";
        } else if (error != null) {
            // 其他登入錯誤
            model.addAttribute("errorType", "login_failed");
            model.addAttribute("errorTitle", "登入失敗");
            model.addAttribute("errorMessage", "登入過程中發生錯誤");
            model.addAttribute("errorDescription", "請檢查您的認證信息或稍後再試。");
            model.addAttribute("errorCode", error.toUpperCase());
            return "auth-error";
        }
        
        // 沒有錯誤參數，重定向到首頁
        return "redirect:/";
    }
}
