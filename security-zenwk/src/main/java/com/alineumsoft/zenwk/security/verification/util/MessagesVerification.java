package com.alineumsoft.zenwk.security.verification.util;

import com.alineumsoft.zenwk.security.common.exception.enums.CoreExceptionEnum;
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
	public String getMessage() {
		try {
			return MessageSourceAccessorComponent.getMessage(messageKey);
		} catch (Exception e) {
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeMessage(messageKey));
		}

	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getMessage(String... params) {
		try {
			return MessageSourceAccessorComponent.getMessage(messageKey, params);
		} catch (Exception e) {
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeMessage(messageKey));
		}
	}

}
