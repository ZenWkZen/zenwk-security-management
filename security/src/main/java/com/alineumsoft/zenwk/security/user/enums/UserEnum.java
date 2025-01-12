package com.alineumsoft.zenwk.security.user.enums;

import com.alineumsoft.zenwk.security.user.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserEnum
 */
public enum UserEnum {
	USER_PERSON_NAME("person.name"), 
	USER_PERSON_FIRST_NAME("person.firstUsurname"),
	PERSON_NAME("name"), 
	PERSON_FIRST_NAME("firstUsurname");

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
		return MessageSourceAccessorComponent.getMessage(messageKey);
	}

	/**
	 * @return
	 */
	public String getMessageKey() {
		return messageKey;
	}

}
