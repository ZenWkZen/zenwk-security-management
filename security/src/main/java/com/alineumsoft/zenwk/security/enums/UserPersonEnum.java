package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserEnum
 */
public enum UserPersonEnum {
	USER_USERNAME("username"), 
	USER_EMAIL("email"),
	PERSON_FIRST_NAME("firstName"), 
	PERSON_LAST_NAME("lastName");

	private final String messageKey;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageKey
	 */
	UserPersonEnum(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getDescription() {
		return MessageSourceAccessorComponent.getMessage(messageKey);
	}

	/**
	 * @return
	 */
	public String getMessageKey() {
		return messageKey;
	}

}
