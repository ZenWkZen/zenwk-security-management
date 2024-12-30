package com.alineumsoft.zenwk.security.user.messages.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.messages.enums.MessageKeyEnum;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class MessageService
 */
@Service
public class MessageService {
	private final MessageSource messageSource;

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Constructor
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageSource
	 */
	public MessageService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Obtiene un mensaje formateado
	 * basado en una clave de mensaje y parámetros opcionales.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param key
	 * @param params
	 * @return
	 */
	public String getMessage(MessageKeyEnum key, String... params) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key.getKey(), params, locale);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Obtiene un mensaje formateado
	 * basado en una clave de mensaje (sin param).
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param key
	 * @return
	 */
	public String getMessage(MessageKeyEnum key) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key.getKey(), null, locale);
	}
}
