package com.alineumsoft.zenwk.security.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alineumsoft.zenwk.security.auth.Service.PermissionService;
import com.alineumsoft.zenwk.security.auth.jwt.JwtAuthenticationFilter;
import com.alineumsoft.zenwk.security.common.enums.PermissionOperationEnum;
import com.alineumsoft.zenwk.security.dto.PermissionDTO;
import com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum;
import com.alineumsoft.zenwk.security.person.repository.RolePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Configuración de seguridad para el control de acceso basado en permisos y
 * roles. Define la configuración de Spring Security y la asignación de permisos
 * a las rutas.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class SecurityConfiguration
 */
@Configuration
@Slf4j
public class SecurityFilterChainConfiguration {
	/**
	 * Componenete para jwt
	 */
	private final JwtAuthenticationFilter jwtAuthFilter;
	/**
	 * AuthenticationProvider para la autenticacion del usuario
	 */
	private final AuthenticationProvider authenticationProvider;
	/**
	 * Servicio para la gestion de permisos
	 */
	private final PermissionService permService;
	/**
	 * Manejador para los errores de acceso denegado
	 */
	private final AccessDeniedHandler customAccessDeniedHandler;

	/**
	 * <p>
	 * Constructor que inyecta el repositorio de permisos de roles.
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolePermRepo
	 * @param jwtAuthFilter
	 * @param authenticationProvider
	 * @param permService
	 * @param customAccessDeniedHandler
	 */
	public SecurityFilterChainConfiguration(RolePermissionRepository rolePermRepo,
			JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider,
			PermissionService permService, AccessDeniedHandler customAccessDeniedHandler) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
		this.permService = permService;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Configura la seguridad de la
	 * aplicación utilizando Spring Security. Define las reglas de autorización de
	 * acceso a los endpoints agrupando por operación.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		final Map<PermissionOperationEnum, List<PermissionDTO>> maRolPermissions = permService.getOperationPermission();

		http.authorizeHttpRequests(request -> {
			request.requestMatchers(HttpMethodResourceEnum.USER_CREATE.getMethod(),
					HttpMethodResourceEnum.USER_CREATE.getResource()).permitAll()
					.requestMatchers(HttpMethodResourceEnum.AUTH_LOGIN.getResource()).permitAll()
					.requestMatchers(HttpMethodResourceEnum.VERIFICATION_TOKEN.getMethod(),
							HttpMethodResourceEnum.VERIFICATION_TOKEN.getResource())
					.permitAll();
			// Se agregan los filtros restantes
			addAuthorizationForOperation(request, maRolPermissions);
			request.anyRequest().authenticated();
		}).csrf(csrf -> csrf.disable()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				// Configuración con el handler personalizado
				// Captura el error y muestra un mensaje descriptivo con la causa
				.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Agrega restricciones solo si hay
	 * permisos en el mapa
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param maRolPermissions
	 */
	private void addAuthorizationForOperation(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request,
			Map<PermissionOperationEnum, List<PermissionDTO>> maRolPermissions) {
		if (!maRolPermissions.isEmpty()) {
			request.requestMatchers(getRequestMatchersForOperation(PermissionOperationEnum.CREATE, maRolPermissions))
					.hasAnyAuthority(getRoles(maRolPermissions.get(PermissionOperationEnum.CREATE)))
					.requestMatchers(getRequestMatchersForOperation(PermissionOperationEnum.UPDATE, maRolPermissions))
					.hasAnyAuthority(getRoles(maRolPermissions.get(PermissionOperationEnum.UPDATE)))
					.requestMatchers(getRequestMatchersForOperation(PermissionOperationEnum.DELETE, maRolPermissions))
					.hasAnyAuthority(getRoles(maRolPermissions.get(PermissionOperationEnum.DELETE)))
					.requestMatchers(getRequestMatchersForOperation(PermissionOperationEnum.LIST, maRolPermissions))
					.hasAnyAuthority(getRoles(maRolPermissions.get(PermissionOperationEnum.LIST)))
					.requestMatchers(
							getRequestMatchersForOperation(PermissionOperationEnum.GET, maRolPermissions))
					.hasAnyAuthority(getRoles(maRolPermissions.get(PermissionOperationEnum.GET)));
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene los `RequestMatcher`
	 * correspondientes a una operación especifica, eliminando rutas duplicadas.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param operation
	 * @param mapPermission
	 * @return
	 */
	private RequestMatcher[] getRequestMatchersForOperation(PermissionOperationEnum operation,
			Map<PermissionOperationEnum, List<PermissionDTO>> mapPermission) {
		// Obtener los permisos asociados al rol
		List<PermissionDTO> listPermissions = mapPermission.get(operation);

		// Si no hay permisos, devolver un array vacio
		if (listPermissions == null || listPermissions.isEmpty()) {
			return new RequestMatcher[0];
		}

		// Elminar permisos duplicados por la consulta.
		// Convertir los permisos en RequestMatchers[].
		return listPermissions.stream()
				.map(perm -> new AntPathRequestMatcher(perm.getResource(), perm.getMethod().toUpperCase()))
				.collect(Collectors.toCollection(LinkedHashSet::new)).toArray(RequestMatcher[]::new);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene los roles asociados a una
	 * lista de permisos eliminando duplicados.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param listPermission
	 * @return
	 */
	private String[] getRoles(List<PermissionDTO> listPermission) {
		// Elimina roles duplicados por la consulta original.
		// Convierte una lista de roles en un String[]
		return listPermission.stream().map(PermissionDTO::getName)
				.collect(Collectors.toCollection(LinkedHashSet::new)).toArray(String[]::new);

	}

}
