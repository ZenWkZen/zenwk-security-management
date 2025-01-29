package com.alineumsoft.zenwk.security.config;

import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.PERSON_CREATE;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.PERSON_DELETE;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.PERSON_FIND_ALL;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.PERSON_FIND_BY_ID;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.PERSON_UPDATE;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.USER_CREATE;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.USER_DELETE;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.USER_FIND_ALL;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.USER_FIND_BY_ID;
import static com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum.USER_UPDATE;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.APP_ADMIN;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.AUDITOR;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.NEW_USER;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.SECURITY_ADMIN;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.SYSTEM_ADMIN;
import static com.alineumsoft.zenwk.security.enums.RoleEnum.USER;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class SecurityConfiguration
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfiguration {

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

		http.authorizeHttpRequests(request -> request
				.requestMatchers(matchersForCreate()).hasAnyAuthority(roleCreate())
				.requestMatchers(matchersForUpdate()).hasAnyAuthority(roleUpdate())
				.requestMatchers(matchersForDelete()).hasAnyAuthority(roleDelete())
				.requestMatchers(matchersForFindAll()).hasAnyAuthority(roleFindAll())
				.requestMatchers(matchersForFindById()).hasAnyAuthority(roleFinById())
				// Todas las solicitudes deben estar autenticadas
				.anyRequest().authenticated()
			)
			.httpBasic(Customizer.withDefaults())
			.csrf(csrf -> csrf.disable());
		return http.build();
	}

	/**
	 * <p>
	 * <b> Security: </b> Role create
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static String[] roleCreate() {
		return new String[] { NEW_USER.name(), SYSTEM_ADMIN.name() };
	}

	/**
	 * <p>
	 * <b> Security: </b> Role update
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static String[] roleUpdate() {
		return new String[] { USER.name(), SYSTEM_ADMIN.name(), APP_ADMIN.name(), SECURITY_ADMIN.name() };
	}

	/**
	 * <p>
	 * <b> Security: </b> Role delete
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static String[] roleDelete() {
		return new String[] { USER.name(), SYSTEM_ADMIN.name(), SECURITY_ADMIN.name() };
	}

	/**
	 * <p>
	 * <b> Security: </b> Role find by id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static String[] roleFinById() {
		return new String[] { USER.name(), SYSTEM_ADMIN.name(), APP_ADMIN.name(), SECURITY_ADMIN.name(),
				AUDITOR.name() };
	}

	/**
	 * <p>
	 * <b> Security: </b> Role find all
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static String[] roleFindAll() {
		return new String[] { SYSTEM_ADMIN.name(), SECURITY_ADMIN.name(), AUDITOR.name() };
	}

	/**
	 * <p>
	 * <b> Security: </b> RequestMatcher para create
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private RequestMatcher[] matchersForCreate() {
		return new RequestMatcher[] {
				new AntPathRequestMatcher(USER_CREATE.getResource(), USER_CREATE.getMethod().name()),
				new AntPathRequestMatcher(PERSON_CREATE.getResource(), PERSON_CREATE.getMethod().name()) };
	}

	/**
	 * <p>
	 * <b> Security: </b> RequestMatcher para update
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static RequestMatcher[] matchersForUpdate() {
		return new RequestMatcher[] {
				new AntPathRequestMatcher(USER_UPDATE.getResource(), USER_UPDATE.getMethod().name()),
				new AntPathRequestMatcher(PERSON_UPDATE.getResource(), PERSON_UPDATE.getMethod().name()) };
	}

	/**
	 * <p>
	 * <b> Security: </b> RequestMatcher para delete
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static RequestMatcher[] matchersForDelete() {
		return new RequestMatcher[] {
				new AntPathRequestMatcher(USER_DELETE.getResource(), USER_DELETE.getMethod().name()),
				new AntPathRequestMatcher(PERSON_DELETE.getResource(), PERSON_DELETE.getMethod().name()) };
	}

	/**
	 * <p>
	 * <b> Security: </b> RequestMatcher para find all
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static RequestMatcher[] matchersForFindAll() {
		return new RequestMatcher[] {
				new AntPathRequestMatcher(USER_FIND_ALL.getResource(), USER_FIND_ALL.getMethod().name()),
				new AntPathRequestMatcher(PERSON_FIND_ALL.getResource(), PERSON_FIND_ALL.getMethod().name()) };
	}

	/**
	 * <p>
	 * <b> Security: </b> RequestMatcher para find by id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private static RequestMatcher[] matchersForFindById() {
		return new RequestMatcher[] {
				new AntPathRequestMatcher(USER_FIND_BY_ID.getResource(), USER_FIND_BY_ID.getMethod().name()),
				new AntPathRequestMatcher(PERSON_FIND_BY_ID.getResource(), PERSON_FIND_BY_ID.getMethod().name()) };
	}

}
