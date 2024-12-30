package com.alineumsoft.zenwk.security.user.enums;

import com.alineumsoft.zenwk.security.user.config.MessageSourceAccessor;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserEnum
 */
public enum UserEnum {
	PERSON_NAME("person.firstUsurname"), 
	PERSON_FIRST_NAME("person.name");

	private final String messageKey;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageKey
	 */
	UserEnum(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getDescription() {
		return MessageSourceAccessor.getMessage(messageKey);
	}

	/**
	 * @return
	 */
	public String getMessageKey() {
		return messageKey;
	}

}
