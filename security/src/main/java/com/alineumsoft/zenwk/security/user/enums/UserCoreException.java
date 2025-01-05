package com.alineumsoft.zenwk.security.user.enums;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ErrorCodeException
 */
public enum UserCoreException {
	// fun/tec_entidad_operacion_campo/proceso_descripcion
	FUNC_USER_CREATE_EMAIL_UNIQUE("F01", "functional.user.create.email.unique"),
	FUNC_USER_CREATE_NO_FOUND("F01", "functional.user.create.nofound");

	private String code;
	private String key;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param codeException
	 * @param keyMessage
	 */
	private UserCoreException(String codeException, String keyMessage) {
		this.code = codeException;
		this.key = keyMessage;

	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getMessage() {
		return MessageSourceAccessorComponent.getMessage(key);
	}

	/**
	 * <p>
	 * <b> Genreal Exception </b> Recupera el mensaje incluyendo el codigo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCodeMessage() {
		return new StringBuilder(code).append(CommonMessageConstants.SEPARATOR_CODE)
				.append(MessageSourceAccessorComponent.getMessage(key)).toString();

	}

}
