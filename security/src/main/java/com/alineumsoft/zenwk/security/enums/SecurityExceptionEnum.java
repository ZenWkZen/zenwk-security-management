package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.enums.CoreExceptionEnum;
import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ErrorCodeException
 */
public enum SecurityExceptionEnum {
	// fun/tec_entidad_operacion_campo/proceso_descripcion
	FUNC_USER_CREATE_EMAIL_UNIQUE("FUNC_SEC_001", "functional.user.create.email.unique"),
	FUNC_USER_NOT_FOUND("FUNC_SEC_002", "functional.user.notfound"),
	FUNC_USER_MAIL_EXISTS("FUNC_SEC_003", "functional.user.email.exists"),
	FUNC_PERSON_NOT_FOUND("FUNC_SEC_004", "functional.person.notfound"),
	FUNC_PERSON_EXIST("FUNC_SEC_005", "functional.person.exist"),
	FUNC_USER_EXIST("FUNC_SEC_006", "functional.user.exist"),
	FUNC_ROLE_NOT_EXIST("FUNC_SEC_007", "functional.role.not.exist");

	private String code;
	private String key;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param codeException
	 * @param keyMessage
	 */
	private SecurityExceptionEnum(String code, String keyMessage) {
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
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeDescription(key));
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
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeDescription(key));
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
