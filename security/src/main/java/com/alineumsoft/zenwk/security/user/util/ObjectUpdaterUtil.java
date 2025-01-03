package com.alineumsoft.zenwk.security.user.util;

import java.lang.reflect.Field;

import com.alineumsoft.zenwk.security.user.util.message.component.MessageSourceAccessorComponent;
import com.alineumsoft.zenwk.security.user.util.messages.enums.MessageKeyUtilEnum;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ObjectUpdater
 */
public final class ObjectUpdaterUtil {

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
	static public <T> void updateDataObject(T source, T target) {
		if (source == null || target == null) {
			throw new IllegalArgumentException(
					MessageSourceAccessorComponent.getMessage(MessageKeyUtilEnum.DATA_UPDATE_NULL.getKey()));
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
				throw new RuntimeException(MessageSourceAccessorComponent
						.getMessage(MessageKeyUtilEnum.DATA_UPDATE_FAILED.getKey(), field.getName(), e.getMessage()));
			}
		}
	}
}
