package com.niqdev.authclient.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
	
    @GetMapping("/")
    public String home(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("user", oidcUser);
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
        return "index";
    }
    
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("user", oidcUser);
            
            // 安全地處理用戶聲明
            try {
                Map<String, Object> claimsMap = new HashMap<>();
                if (oidcUser.getClaims() != null) {
                    oidcUser.getClaims().forEach((key, value) -> {
                        claimsMap.put(key, value != null ? value.toString() : "null");
                    });
                }
                model.addAttribute("claimsMap", claimsMap);
            } catch (Exception e) {
                model.addAttribute("claimsMap", new HashMap<>());
            }
            
            // 安全地處理令牌
            try {
                if (oidcUser.getIdToken() != null) {
                    model.addAttribute("idTokenValue", oidcUser.getIdToken().getTokenValue());
                } else {
                    model.addAttribute("idTokenValue", "無法獲取令牌");
                }
            } catch (Exception e) {
                model.addAttribute("idTokenValue", "無法獲取令牌");
            }
            
            return "profile";
        }
        return "redirect:/";
    }
    
    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("logoutMessage", "您已成功登出");
        model.addAttribute("logoutTime", java.time.LocalDateTime.now());
        return "logout-success";
    }
    
    @GetMapping("/logout-confirm")
    public String logoutConfirm(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("user", oidcUser);
            return "logout-confirm";
        }
        return "redirect:/";
    }
}
