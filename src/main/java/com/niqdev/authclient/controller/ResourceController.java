package com.niqdev.authclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ResourceController {
	
	@Autowired
	private WebClient oauth2WebClient;
	
    @GetMapping("/resource")
    public String resource(@RegisteredOAuth2AuthorizedClient("oidc-client") OAuth2AuthorizedClient authorizedClient) {
        String response = oauth2WebClient
                .get()
                .uri("http://localhost:8081/api/resource")
                .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 在 WebFlux 也可以用 block() 轉同步

        return response;
    }
    
    @GetMapping("/debug")
    public String debug(@RegisteredOAuth2AuthorizedClient("oidc-client") OAuth2AuthorizedClient authorizedClient) {
        String response = oauth2WebClient
                .get()
                .uri("http://localhost:8081/api/debug")
                .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 在 WebFlux 也可以用 block() 轉同步

        return response;
    }
}
