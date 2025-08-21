package com.niqdev.authclient.config;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpCookieOAuth2AuthorizationRequestRepository
		implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final int COOKIE_EXPIRE_SECONDS = 180;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
				.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);
	}

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

	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		// 如果只傳 request，調用帶 response 的方法
		return removeAuthorizationRequest(request, null);
	}

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