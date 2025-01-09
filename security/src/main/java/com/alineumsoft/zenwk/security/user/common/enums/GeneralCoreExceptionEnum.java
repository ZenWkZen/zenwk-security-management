package com.alineumsoft.zenwk.security.user.common.enums;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.message.component.MessageSourceAccessorComponent;

/**
 * <b>Enum que define las excepciones tecnicas y funcionales a nivel general del
 * core</b>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class GeneralCoreExceptionEnum
 */
public enum GeneralCoreExceptionEnum {
	TECH_HISTORICAL_ENTITY_NOT_FOUND("TECH_GEN_001", "error.historical.entity.notfound"),
	TECH_MESSAGE_NOT_FOUND("TECH_GEN_002", "error.enum.message.notfound");

	private String code;
	private String messageKey;

	/**
	 * @param code
	 * @param messageKey
	 */
	private GeneralCoreExceptionEnum(String code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	/**
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		try {
			return MessageSourceAccessorComponent.getMessage(messageKey);
		} catch (Exception e) {
			throw new RuntimeException(GeneralCoreExceptionEnum.TECH_MESSAGE_NOT_FOUND.getCodeDescription());
		}
	}

	/**
	 * <p>
	 * <b> General core exception: </b> Obtiene un mensaje formateado basado en una
	 * clave de mensaje y parámetros opcionales.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param params
	 * @return
	 */
	public String getMessage(String... params) {
		try {
			return MessageSourceAccessorComponent.getMessage(messageKey, params);
		} catch (Exception e) {
			throw new RuntimeException(GeneralCoreExceptionEnum.TECH_MESSAGE_NOT_FOUND.getCodeDescription(messageKey));
		}
	}

	/**
	 * 
	 * <p>
	 * <b> General core exception: </b> Descripción con codigo de la excepcion
	 * generada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCodeDescription() {
		return String.format(CommonMessageConstants.FORMAT_EXCEPTION, getCode(), getMessage());
	}

	/**
	 * 
	 * <p>
	 * <b> General core exception: </b> Descripción con codigo de la excepcion
	 * generada con parametros
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCodeDescription(String... params) {
		return String.format(CommonMessageConstants.FORMAT_EXCEPTION, getCode(), getMessage(params));
	}

}
