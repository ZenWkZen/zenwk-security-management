package com.alineumsoft.zenwk.security.common.constants;

/**
 * <p>
 * Constantes utilizadas en la autenticacion y autorizacion
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class AuthConfig
 */
public final class AuthConfigConstants {
	/**
	 * class: JwtProvider
	 */
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_BEARER = "Bearer";
	public static final int INDEX_TOKEN = 7;
	public static final String JWT_EXPIRATION_TIME = "${security.jwt.expiration-time}";
	public static final String JWT_SECRET_KEY = "${security.jwt.secret}";
	public static final String JWT_ROLES = "roles";
	public static final String JWT_URLS_ALLOWED_ROL_USER = "urlsRolUser";
	public static final String JWT_ID_USER = "idUser";
	public static final String JWT_USER_STATE = "idState";
	/**
	 * class: PermissionService
	 */
	public final static String ID = "{id}";
	public final static String URL_USER = "user";
	public final static String URL_PERSON = "person";
	/**
	 * class: JwtAuthenticationFilter
	 */
	public static final String PERMISSION = "permissions";
	public static final String WEB_DETAILS = "webDetails";
}
