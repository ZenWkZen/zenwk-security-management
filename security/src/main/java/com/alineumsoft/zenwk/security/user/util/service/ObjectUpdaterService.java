package com.alineumsoft.zenwk.security.user.util.service;

import java.lang.reflect.Field;

import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.messages.enums.MessageKeyEnum;
import com.alineumsoft.zenwk.security.user.messages.service.MessageService;

import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ObjectUpdater
 */
@Data
@Service
public class ObjectUpdaterService {
	private final MessageService messageService;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param messageService
	 */
	public ObjectUpdaterService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * <p>
	 * <b>Util </b> Metodo para actualizar un objecto del mismo tipo descriminando
	 * por datos nulos e iguales.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param source
	 * @param target
	 */
	public <T> void updateDataObject(T source, T target) {
		if (source == null || target == null) {
			throw new IllegalArgumentException(messageService.getMessage(MessageKeyEnum.DATA_UPDATE_NULL));
		}

		// Campos de la clase del objeto
		Field[] fields = source.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);

			try {
				Object value = field.get(source);

				if (value != null) {
					field.set(target, value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						messageService.getMessage(MessageKeyEnum.DATA_UPDATE_FAILED, field.getName(), e.getMessage()));
			}
		}
	}
}
