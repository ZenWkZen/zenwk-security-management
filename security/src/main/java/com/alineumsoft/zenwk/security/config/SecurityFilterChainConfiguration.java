package com.alineumsoft.zenwk.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.alineumsoft.zenwk.security.enums.RoleEnum;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class SecurityConfiguration
 */
@Configuration
public class SecurityFilterChainConfiguration {
	private final static String USER_URL_PATTERN = "/user/**";
	private final static String PERSON_URL_PATTERN = "/person/**";

	/**
	 * <p>
	 * <b> Security: </b> autenticacion basica
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				request -> request.requestMatchers(USER_URL_PATTERN).hasAuthority(RoleEnum.USER.name())
						.requestMatchers(PERSON_URL_PATTERN).hasAuthority(RoleEnum.USER.name()))
				.httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
		return http.build();
	}

}
