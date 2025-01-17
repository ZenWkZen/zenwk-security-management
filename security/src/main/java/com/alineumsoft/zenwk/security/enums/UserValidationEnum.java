package com.alineumsoft.zenwk.security.enums;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonValidationEnum
 */
public enum UserValidationEnum {
	ID_USER_NOT_NULL("validation.person.iduser.notnull");

	private final String code;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param code
	 */
	private UserValidationEnum(String code) {
		this.code = code;
	}

	/**
	 * Gets the value of code.
	 * 
	 * @return the value of code.
	 */
	public String getCode() {
		return code;
	}

}
