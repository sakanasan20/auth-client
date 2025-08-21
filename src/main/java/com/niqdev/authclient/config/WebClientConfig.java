package com.niqdev.authclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	
	@Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

	@Bean
	WebClient oauth2WebClient(ClientRegistrationRepository clientRegistrations,
			OAuth2AuthorizedClientRepository authorizedClients) {
		
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = 
				new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
		
		oauth2.setDefaultOAuth2AuthorizedClient(true); // 預設使用授權的 client
		
		return WebClient.builder()
				.apply(oauth2.oauth2Configuration())
				.build();
	}
}
