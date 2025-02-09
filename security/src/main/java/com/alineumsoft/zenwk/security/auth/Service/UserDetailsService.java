package com.alineumsoft.zenwk.security.auth.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alineumsoft.zenwk.security.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

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
	 * Contador de tiempo de la solicitud
	 */
	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

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
		UserDetails userDetail = null;
		User.UserBuilder userBuilder = User.builder();
		HttpServletRequest request = getCurrentHttpRequest();
		startTime.set(System.currentTimeMillis());
		// Consulta de los roles asociados al usuario
		List<String> rolesName = userService.findRolesByUsername(username, startTime.get(), request).stream()
				.map(rol -> rol.getName().name()).collect(Collectors.toList());
		// Se recupera el usuario desde la bd
		com.alineumsoft.zenwk.security.user.entity.User userEntity = userService.findByUsername(username);
		// Se construye UserDetails y se cargan los roles
		userDetail = userBuilder.username(userEntity.getUsername()).password(userEntity.getPassword())
				.authorities(rolesName.toArray(new String[0])).build();
		// Se retorna el user details que se usara de forma transversal en el sistema
		return userDetail;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene la solicitud HTTP actual.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs != null ? attrs.getRequest() : null;
	}

}
