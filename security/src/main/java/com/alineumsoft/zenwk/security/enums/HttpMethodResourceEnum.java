package com.alineumsoft.zenwk.security.enums;

import org.springframework.http.HttpMethod;

/**
 * <p>
 * Enum que define los recursos expuestos y sus metodos http para el api de
 * seguridad a ser usado en RBAC definido.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class HttpMethodResourceEnum
 */
public enum HttpMethodResourceEnum {
	/**
	 * Recursos par ala entidad usuarios
	 */
	USER_CREATE(HttpMethod.POST, "/api/user"),
	USER_UPDATE(HttpMethod.PUT, "/api/user/{id}"),
	USER_DELETE(HttpMethod.DELETE, "/api/user/{id}"),
	USER_FIND_ALL(HttpMethod.GET, "/api/user"),
	USER_FIND_BY_ID(HttpMethod.GET, "/api/user/{id}"),
	/**
	 * Recursos para la entidad persona
	 */
	PERSON_CREATE(HttpMethod.POST, "/api/person"),
	PERSON_UPDATE(HttpMethod.PUT, "/api/person/{id}"),
	PERSON_DELETE(HttpMethod.DELETE, "/api/person/{id}"),
	PERSON_FIND_ALL(HttpMethod.GET, "/api/person"),
	PERSON_FIND_BY_ID(HttpMethod.GET, "/api/person/{id}"),
	/**
	 * Auth
	 */
	AUTH_LOGIN(HttpMethod.POST, "/api/auth/login"),
	AUTH_LOGOUT(HttpMethod.DELETE, "/api/auth/logout");

	/**
	 * Metodo http
	 */
	private final HttpMethod method;
	/**
	 * recurso
	 */
	private final String resource;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param method
	 * @param resource
	 */
	private HttpMethodResourceEnum(HttpMethod method, String resource) {
		this.method = method;
		this.resource = resource;
	}

	/**
	 * Gets the value of method.
	 * 
	 * @return the value of method.
	 */
	public HttpMethod getMethod() {
		return method;
	}

	/**
	 * Gets the value of resource.
	 * 
	 * @return the value of resource.
	 */
	public String getResource() {
		return resource;
	}

}
