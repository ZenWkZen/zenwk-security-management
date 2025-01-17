package com.alineumsoft.zenwk.security.enums;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class ServiceNameEnum
 */
public  enum SecurityServiceNameEnum {
	USER_CREATE("user.create"),
	USER_UPDATE("user.update"),
	USER_DELETE("user.delete"),
	USER_FIND_BY_ID("user.findById"),
	USER_FIND_ALL("user.findAll"),
	PERSON_CREATE("person.create"),
	PERSON_UPDATE("person.update"),
	PERSON_DELETE("person.delete"),
	PERSON_FIND_BY_ID("person.findById"),
	PERSON_FIND_ALL("person.findAll");
	
	private String code;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param code
	 */
	private SecurityServiceNameEnum(String code) {
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
