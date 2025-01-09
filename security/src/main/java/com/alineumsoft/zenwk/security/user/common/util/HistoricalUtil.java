package com.alineumsoft.zenwk.security.user.common.util;

import com.alineumsoft.zenwk.security.user.common.component.AppContextHolderComponent;
import com.alineumsoft.zenwk.security.user.common.constants.UtilConstants;
import com.alineumsoft.zenwk.security.user.common.enums.GeneralCoreExceptionEnum;
import com.alineumsoft.zenwk.security.user.common.hist.enums.HistoricalOperationEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * <b>Clase utilitaria para la gestion del log transaccional o historico</b>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class HistoricalUtil
 */
@Slf4j
public final class HistoricalUtil {
	/**
	 * <p>
	 * Constructor
	 * </p>
	 */
	private HistoricalUtil() {

	}

	/**
	 * <p>
	 * <b> Util historico: </b> Metodo generico para el registro historico
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param operation
	 */
	public static <T> void registerHistorical(T entity, HistoricalOperationEnum operation, Class<?> classService) {
		try {
			Object targetService = AppContextHolderComponent.getBean(classService);
			targetService.getClass()
					.getMethod(UtilConstants.METHOD_SAVE_HISTORICAL, entity.getClass(), HistoricalOperationEnum.class)
					.invoke(targetService, entity, operation);

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(GeneralCoreExceptionEnum.TECH_HISTORICAL_ENTITY_NOT_FOUND
					.getCodeDescription(entity.getClass().getSimpleName()));
		}
	}

}
