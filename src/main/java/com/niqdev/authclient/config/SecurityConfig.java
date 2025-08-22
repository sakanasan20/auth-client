package com.niqdev.authclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login**", "/logout-success", "/auth-error").permitAll()
                .requestMatchers("/logout-confirm").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                // 設定使用 cookie 儲存授權請求
                .authorizationEndpoint(authorization -> authorization
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                )
                // 設定自訂的失敗處理器
                .failureHandler((request, response, exception) -> {
                    // 將 OAuth2 錯誤重定向到自訂錯誤頁面
                    String error = "login_failed";
                    if (exception.getMessage().contains("access_denied")) {
                        error = "access_denied";
                    }
                    response.sendRedirect("/auth-error?error=" + error);
                })
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
            );

        return http.build();
    }
    
    @Bean
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}
