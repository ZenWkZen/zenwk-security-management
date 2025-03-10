package com.alineumsoft.zenwk.security.enums;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.enums.CoreExceptionEnum;
import com.alineumsoft.zenwk.security.common.message.component.MessageSourceAccessorComponent;

import lombok.Getter;

/**
 * <p>
 * Enum para los mensajes a mostrar cuando se generan excepciones funcionales
 * predefinidas en el sistema.
 * <p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ErrorCodeException
 */
@Getter
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
	FUNC_PERSON_ID_USER_IS_ASSOCIATE("FUNC_SEC_011", "functional.person.iduser.isassociate"),
	/**
	 * Role
	 */
	FUNC_ROLE_EXISTS("FUNC_SEC_012", "functional.role.exist"),
	FUNC_ROLE_NOT_EXISTS("FUNC_SEC_013","common.exception.rol.noexists"),
	TECH_ROLE_NOT_DELETE("TECH_SEC_001","functional.exception.rol.nodelete"),
	FUNC_ROLE_ASSIGNMENT_PERMISSION_EXISTS("FUNC_SEC_016","functional.exception.rol.assignment.exists"),
	FUNC_ROLE_ASSIGNMENT_PERMISSION_NOT_EXISTS("FUNC_SEC_017","functional.exception.rol.assignment.notexists"),
	/**
	 * Permission
	 */
	FUNC_PERMISSION_EXIST("FUNC_SEC_014", "functional.permission.exist"),
	FUNC_PERMISSION_NOT_EXISTS("FUNC_SEC_015","functional.permission.noexists"),
	TECH_PERMISSION_NOT_DELETE("TECH_SEC_002","functional.permission.notdelete"),
	/**
	 * Verificación 
	 */
	FUNC_VERIFICATION_TOKEN_EXPIRATION("FUNC_SEC_0116","functional.verification.token.expiration"),
	FUNC_VERIFICATION_TOKEN_NOT_FOUND("FUNC_SEC_0117","functional.verification.token.notfound");
	/**
	 * code
	 */
	private final String code;
	/**
	 * key
	 */
	private final String key;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param codeException
	 * @param keyMessage
	 */
	SecurityExceptionEnum(String code, String keyMessage) {
		this.code = code;
		this.key = keyMessage;

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
