package com.alineumsoft.zenwk.security.user.common.util;

import java.lang.reflect.Field;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ObjectUpdater
 */
@Slf4j
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
	static public <T> void updateDataEqualObject(T source, T target) {
		validateObjects(source, target);

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
				log.warn(e.getMessage());
			}
		}
	}

	/**
	 * <p>
	 * <b> Util. </b> Valida si un uno de los objectos es nulo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param source
	 * @param target
	 */
	private static <T> void validateObjects(T source, T target) {
		if (source == null || target == null) {
			throw new IllegalArgumentException(CommonMessageConstants.ILEGAL_ARGUMENT_EXCEPTION);
		}
	}
}
