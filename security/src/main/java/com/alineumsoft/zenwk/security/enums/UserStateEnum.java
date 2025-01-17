package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserStateEnum
 */
public enum UserStateEnum {
	INCOMPLETE_PERFIL("user.state.icompleteperfil"),
	ACTIVE("user.state.active"), 
	DISABLE("user.state.disabled");

	private final String messageKey;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageKey
	 */
	UserStateEnum(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * <p>
	 * <b> CU001_XX </b> Obtiene la descripción del mensaje usando la clave desde
	 * messages.properties.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageSource
	 * @return
	 */
	 public String getDescription() {
	        return MessageSourceAccessorComponent.getMessage(messageKey);
	    }

	/**
	 * <p>
	 * <b> CU001_XX </b> Devuelve la clave del mensaje asociada al enum.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getMessageKey() {
		return messageKey;
	}
}
