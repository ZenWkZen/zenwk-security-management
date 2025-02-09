package com.alineumsoft.zenwk.security.constants;

/**
 * <p>
 * Contiene las claves de los mensajes de validación utilizados en los DTOs para
 * anotaciones de validación como {@code @Valid}, {@code @NotNull},
 * {@code @Size}, entre otras. Estas claves se utilizan en archivos de
 * internacionalización (i18n) para proporcionar mensajes de error
 * personalizados en las validaciones.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonValidationEnum
 */
public final class DtoValidationKeys {
	public static final String PERSON_ID_USER_NOT_NULL = "validation.person.iduser.notnull";
	public static final String PERSON_ID_USER_NOT_FOUND = "validation.person.iduser.notfound";
	public static final String PERSON_FIRST_NAME_NOT_NULL = "validation.person.firstname.notnull";
	public static final String PERSON_LAST_NAME_NOT_NULL = "validation.person.lastname.notnull";
	public static final String PERSON_NAME_INVALID = "validation.person.firstname.invalid";
	public static final String PERSON_LAST_NAME_INVALID = "validation.person.lastname.invalid";
	public static final String PERSON_PHONE_NUMBER_INVALID = "validation.person.phonenumber.invalid";
	public static final String PERSON_DATE_INVALID = "validation.person.date.invalidformat";
	public static final String PERSON_NUMERIC_INVALID = "validation.person.numeric.invalid";
	public static final String PERSON_ALPHANUMERIC_INVALID = "validation.person.alphanumeric.invalid";
	public static final String PERSON_COLOMBIA_ADDRESS = "validation.address.invalidformat";
	public static final String PERSON_URL_INVALID = "validation.person.url.invalid";

	public static final String USER_PASSWORD_INVALID = "validation.user.password.invalid";
	public static final String USER_USERNAME_INVALID = "validation.user.username.invalid";
	public static final String USER_EMAIL_INVALID = "validation.user.email.invalid";
	public static final String USER_EMAIL_NOT_NULL = "validation.user.email.notnull";
	public static final String USER_USERNAME_NOT_NULL = "validation.user.username.notnull";
	public static final String USER_PASSWORD_NOT_NULL = "validation.user.password.notnull";
	public static final String USER_EMAIL_MAX_LENGTH = "validation.user.email.maxlength";
	public static final String USER_PASSWORD_MAX_LENGTH = "validation.user.password.maxlength";
	public static final String USER_USERNAME_MAX_LENGTH = "validation.user.username.maxlength";

}
