package com.alineumsoft.zenwk.security.constants;

/**
 * <p>
 * Refencia a los mensajes de validacion de las entidades del modulo de
 * seguridad. Soporta la internacionalizacion
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonValidationEnum
 */
public final class ValidationKeyMessagesConstants {
	public static final String PERSON_ID_USER_NOT_NULL = "validation.person.iduser.notnull";
	public static final String PERSON_ID_USER_NOT_FOUND = "validation.person.iduser.notfound";
	public static final String PERSON_FIRST_NAME_NOT_NULL = "validation.person.firstname.notnull";
	public static final String PERSON_LAST_NAME_NOT_NULL = "validation.person.lastname.notnull";

	public static final String PERSON_NAME_INVALID = "validation.person.firstname.invalid";
	public static final String PERSON_LAST_NAME_INVALID = "validation.person.lastname.invalid";
	public static final String PERSON_EMAIL_INVALID = "validation.person.email.invalid";
	public static final String PERSON_PHONE_NUMBER_INVALID = "validation.person.phonenumber.invalid";
	public static final String PERSON_PASSWORD_INVALID = "validation.person.password.invalid";
	public static final String PERSON_DATE_INVALID = "validation.person.date.invalidformat";
	public static final String PERSON_NUMERIC_INVALID = "validation.person.numeric.invalid";
	public static final String PERSON_ALPHANUMERIC_INVALID = "validation.person.alphanumeric.invalid";
	public static final String PERSON_COLOMBIA_ADDRESS = "validation.address.invalidformat";
	public static final String PERSON_URL_INVALID = "validation.person.url.invalid";
}
