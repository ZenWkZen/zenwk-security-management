package com.alineumsoft.zenwk.security.verification.util;

import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Enum para los mensajes de verificación del usuario en el sistema.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class MessagesVerification
 */
@Getter
@RequiredArgsConstructor
public enum MessagesVerification {
	TOKEN_EMAIL_SUBJECT("verification.token.email.subject");

	/**
	 * messageKey
	 */
	private final String messageKey;

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getDescription() {
		return MessageSourceAccessorComponent.getMessage(messageKey);
	}

}
