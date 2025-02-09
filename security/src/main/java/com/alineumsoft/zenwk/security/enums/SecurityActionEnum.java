package com.alineumsoft.zenwk.security.enums;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class ServiceNameEnum
 */
public  enum SecurityActionEnum {
	USER_CREATE("USER.CREATE"),
    USER_UPDATE("USER.UPDATE"),
    USER_DELETE("USER.DELETE"),
    USER_FIND_BY_ID("USER.FIND_BY_ID"),
    USER_FIND_ALL("USER.FIND_ALL"),
    USER_FIND_ROLE("USER.FIND_ROLE"),
    PERSON_CREATE("PERSON.CREATE"),
    PERSON_UPDATE("PERSON.UPDATE"),
    PERSON_DELETE("PERSON.DELETE"),
    PERSON_FIND_BY_ID("PERSON.FIND_BY_ID"),
    PERSON_FIND_ALL("PERSON.FIND_ALL"),
    AUTH_LOGIN("AUTH.LOGIN"),
    AUTH_LOGOUT("AUTH.LOGOUT");
	
	private String code;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param code
	 */
	private SecurityActionEnum(String code) {
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
