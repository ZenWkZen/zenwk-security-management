package com.alineumsoft.zenwk.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.alineumsoft.zenwk.security.entity.Role;
import com.alineumsoft.zenwk.security.enums.RoleEnum;
import com.alineumsoft.zenwk.security.user.service.UserService;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class SecurityConfiguration
 */
@Configuration
public class SecurityUserDetailsConfiguration {
	private final static String USER_NAME_PROPERTY = "${spring.security.user.name}";
	private final static String USER_PASSWORD_PROPERTY = "${spring.security.user.password}";

	@Value(USER_NAME_PROPERTY)
	private String userName;

	@Value(USER_PASSWORD_PROPERTY)
	private String userPassword;
	/**
	 * Servicio para la gestion de user
	 */
	private final UserService userService;

	/**
	 * <p> <b> CU001_Seguridad_Creacion_Usuario </b> Constructor  </p> 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a> 
	 * @param userService
	 */
	public SecurityUserDetailsConfiguration(UserService userService) {
		this.userService = userService;
	}

	/**
	 * <p>
	 * <b> Security: </b> encripacion de password
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * <p>
	 * <b>Security: </b> usuario ambiente de pruebas desarrollo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param passwordEncoder
	 * @return
	 */
	@Bean
	public UserDetailsService userDetailService(PasswordEncoder passwordEncoder) {
		User.UserBuilder users = User.builder();
		UserDetails userTemp = users.username(userName).password(passwordEncoder.encode(userPassword))
				.authorities(RoleEnum.USER.name()).build();
		return new InMemoryUserDetailsManager(userTemp);
	}

	/**
	 * <p> <b> Security: </b> Recupera el rol del usuario  </p> 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a> 
	 * @param username
	 * @return
	 */
	@Bean
	public UserDetails loadUserByUsername(String username) {
		Role role = userService.getRoleUser(username);
		return null;
	}

}
