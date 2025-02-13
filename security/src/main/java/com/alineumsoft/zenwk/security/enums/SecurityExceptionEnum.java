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
	/**
	 * Auth
	 */
	FUNC_AUTH_TOKEN_INVALID("FUNC_SEC_001", "auth.token.invalid"),
	FUNC_AUTH_URI_FORBIDEN("FUNC_SEC_002", "auth.uri.forbiden"),
	/**
	 * Roles
	 */
	FUNC_ROLE_USER_NOT_EXIST("FUNC_SEC_003", "functional.roleuser.not.exist"),
	/**
	 * User
	 */
	FUNC_USER_CREATE_EMAIL_UNIQUE("FUNC_SEC_004", "functional.user.create.email.unique"),
	FUNC_USER_NOT_FOUND_ID("FUNC_SEC_005", "functional.user.id.notfound"),
	FUNC_USER_NOT_FOUND_USERNAME("FUNC_SEC_006", "functional.user.username.notfound"),
	FUNC_USER_MAIL_EXISTS("FUNC_SEC_007", "functional.user.email.exists"),
	FUNC_USER_EXIST("FUNC_SEC_007", "functional.user.exist"),
	/**
	 * Person
	 */
	FUNC_PERSON_NOT_FOUND("FUNC_SEC_008", "functional.person.notfound"),
	FUNC_PERSON_EXIST("FUNC_SEC_009", "functional.person.exist"),
	FUNC_PERSON_ID_USER_NOT_ASSOCIATE("FUNC_SEC_010", "functional.person.iduser.notassociate"),
	FUNC_PERSON_ID_USER_IS_ASSOCIATE("FUNC_SEC_011", "functional.person.iduser.isassociate");

	/**
	 * code
	 */
	private String code;
	/**
	 * key
	 */
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
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeMessage(key));
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
			throw new RuntimeException(CoreExceptionEnum.TECH_COMMON_MESSAGE_NOT_FOUND.getCodeMessage(key));
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
