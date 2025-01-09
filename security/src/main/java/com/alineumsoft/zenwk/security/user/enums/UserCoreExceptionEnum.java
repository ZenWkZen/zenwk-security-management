package com.alineumsoft.zenwk.security.user.enums;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.enums.GeneralCoreExceptionEnum;
import com.alineumsoft.zenwk.security.user.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ErrorCodeException
 */
public enum UserCoreExceptionEnum {
	// fun/tec_entidad_operacion_campo/proceso_descripcion
	FUNC_USER_CREATE_EMAIL_UNIQUE("FUNC_SEGUSE_001", "functional.user.create.email.unique"),
	FUNC_USER_NOT_FOUND("FUNC_SEGUSE_002", "functional.user.notfound"),
	FUNC_USER_MAIL_EXISTS("FUNC_SEGUSE_003", "functional.user.email.exists");

	private String code;
	private String key;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param codeException
	 * @param keyMessage
	 */
	private UserCoreExceptionEnum(String code, String keyMessage) {
		this.code = code;
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
		try {
			return MessageSourceAccessorComponent.getMessage(key);
		} catch (Exception e) {
			throw new RuntimeException(GeneralCoreExceptionEnum.TECH_MESSAGE_NOT_FOUND.getCodeDescription());
		}
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getMessage(String... params) {
		try {
			return MessageSourceAccessorComponent.getMessage(key, params);
		} catch (Exception e) {
			throw new RuntimeException(GeneralCoreExceptionEnum.TECH_MESSAGE_NOT_FOUND.getCodeDescription());
		}
	}

	/**
	 * <p>
	 * <b> General User Exception. </b> Recupera el mensaje incluyendo el codigo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCodeMessage() {
		return String.format(CommonMessageConstants.FORMAT_EXCEPTION, code, getMessage());
	}

	/**
	 * <p>
	 * <b> General User Exception. </b> Recupera el mensaje incluyendo el codigo con
	 * parametros
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCodeMessage(String... params) {
		return String.format(CommonMessageConstants.FORMAT_EXCEPTION, code, getMessage(params));
	}

}
