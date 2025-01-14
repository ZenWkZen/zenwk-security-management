package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class RolesEnum
 */
public enum RoleEnum {
	SYSTEM_ADMIN("role.security.systemadmin"),
	SECURITY_ADMIN("role.security.securityadmin"),
	APP_ADMIN("role.security.appadmin"), 
	AUDITOR("role.security.auditor"), 
	USER("role.security.user");

	private final String messageKey;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageKey
	 */
	private RoleEnum(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getDesciption() {
		return MessageSourceAccessorComponent.getMessage(messageKey);
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getMessagekey() {
		return messageKey;
	}

}
