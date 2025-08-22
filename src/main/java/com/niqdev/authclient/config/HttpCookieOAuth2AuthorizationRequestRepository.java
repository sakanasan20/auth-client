package com.niqdev.authclient.config;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OAuth2 授權請求的 Cookie 儲存庫
 */
public class HttpCookieOAuth2AuthorizationRequestRepository
		implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	// OAuth2 授權請求的 Cookie 名稱
	public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	
	// Cookie 過期時間（3分鐘）
	public static final int COOKIE_EXPIRE_SECONDS = 180;

	// 從 Cookie 載入授權請求
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
				.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);
	}

	// 將授權請求儲存到 Cookie
	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		if (authorizationRequest == null) {
			CookieUtils.deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
			return;
		}
		CookieUtils.addCookie(response, OAUTH2_AUTH_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest),
				COOKIE_EXPIRE_SECONDS);
	}

	// 移除授權請求（重載方法）
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		// 調用帶 response 的方法
		return removeAuthorizationRequest(request, null);
	}

	// 移除授權請求並清除 Cookie
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
			HttpServletResponse response) {
		OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
		if (response != null) {
			CookieUtils.deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
		}
		return authRequest;
	}
}