package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class PermissionEnum
 */
public enum PermissionEnum {
	CREATE_ENTITY("permission.create.entity"), 
	VIEW_ENTITY("permission.view.entity"),
	UPDATE_ENTITY("permission.update.entity"), 
	DELETE_ENTITY("permission.delete.entity"),
	CREATE_USER("permission.create.user"), 
	VIEW_USER("permission.view.user"), 
	DELETE_USER("permission.delete.user"),
	UPDATE_USER("permission.update.user");

	private final String messageKey;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageKey
	 */
	private PermissionEnum(String messageKey) {
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
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getPermissionKey() {
		return messageKey;
	}
}
