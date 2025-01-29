package com.alineumsoft.zenwk.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.service.UserService;

/**
 * <p>
 * Serivcio para la autenticacion de usuarios
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class UserDetailsService
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	/**
	 * Servicio para la gestion de user
	 */
	private final UserService userService;

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Constructor
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userService
	 */
	public UserDetailsService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<String> rolesName = userService.findRolesByUsername(username).stream().map(rol -> rol.getName().name())
				.collect(Collectors.toList());

		com.alineumsoft.zenwk.security.user.entity.User userEntity = userService.findByUsername(username);
		User.UserBuilder userBuilder = User.builder();
		// Retorno de user details
		UserDetails userDetail = userBuilder.username(userEntity.getUsername()).password(userEntity.getPassword())
				.authorities(rolesName.toArray(new String[0])).build();
		return userDetail;
	}

}
