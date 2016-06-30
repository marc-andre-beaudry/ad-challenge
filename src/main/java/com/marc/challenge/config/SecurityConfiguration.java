package com.marc.challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemorySelfCleaningProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${challengeapi.consumer.key}")
	private String consumerKey;

	@Value("${challengeapi.consumer.secret}")
	private String consumerSecret;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/api/v1/subscription/**").csrf().disable().exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint()).and()
			.authorizeRequests().anyRequest().authenticated().and()
			.addFilterBefore(protectedResourceProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new Http403ForbiddenEntryPoint();
	}

	@Bean
	public OAuthProviderTokenServices oauthProviderTokenServices() {
		return new InMemorySelfCleaningProviderTokenServices();
	}

	@Bean
	public ConsumerDetailsService consumerDetailsService() {
		return new ConsumerDetailsService() {
			@Override
			public ConsumerDetails loadConsumerByConsumerKey(String consumerKey) throws OAuthException {
				BaseConsumerDetails cd = new BaseConsumerDetails();
				cd.setConsumerKey(consumerKey);
				cd.setSignatureSecret(new SharedConsumerSecretImpl(consumerSecret));
				cd.setConsumerName("AppDirect");
				cd.setRequiredToObtainAuthenticatedToken(false);
				return cd;
			}
		};
	}

	@Bean
	public ProtectedResourceProcessingFilter protectedResourceProcessingFilter() {
		ProtectedResourceProcessingFilter filter = new ProtectedResourceProcessingFilter();
		filter.setConsumerDetailsService(consumerDetailsService());
		filter.setTokenServices(oauthProviderTokenServices());
		return filter;
	}

	@Bean
	public ProtectedResourceDetails protectedResourceDetails() {
		BaseProtectedResourceDetails resourceDetails = new BaseProtectedResourceDetails();
		resourceDetails.setConsumerKey(consumerKey);
		SignatureSecret secret = new SharedConsumerSecretImpl(consumerSecret);
		resourceDetails.setSharedSecret(secret);
		return resourceDetails;
	}
}
