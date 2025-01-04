package com.alineumsoft.zenwk.security.user.common.util;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.message.component.MessageSourceAccessorComponent;

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
					MessageSourceAccessorComponent.getMessage(CommonMessageConstants.OBJECT_UPDATE_NULL));
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
				throw new RuntimeException(MessageFormat.format(CommonMessageConstants.OBJECT_UPDATE_FAILED,
						field.getName(), e.getMessage()));
			}
		}
	}
}
