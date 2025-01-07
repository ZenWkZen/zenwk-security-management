package com.alineumsoft.zenwk.security.user.common.constants;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class LogMessageConstants
 */
public final class CommonMessageConstants {
	// Indica que el recurso solicitado no fue encontrado
	public final static String NOT_FOUND = "NOT_FOUND";
	// Indica que no hay contenido para la solicitud
	public final static String NO_CONTENT = "NOT_CONTENT";
	// Indica que no se esperaba un cuerpo en la solicitud o respuesta
	public static final String NOT_APPLICABLE_BODY = "NOT_APPLICABLE_BODY";
	// Indica que la solicitud fue exitosa y no terminó con error
	public static final String REQUEST_SUCCESSFUL = "REQUEST_SUCCESSFUL";

	public static final String ERROR_NOT_NULL = "Value must not be null";
	// Separador mensajes - (code-messsage)
	public static final String SEPARATOR_CODE = "-";
	// Detalles de la excepcion
	public static final String EXCEPTION_DETAILS = " Class: {0}, Method: {1}. Line: {2}, Value: {3}, Expected: {4}. ";
	// Plantilla mensaje technicalExcpetion
	public static final String MSG_EXCEPTION_TECHNICAL = "Technical error: {}";
	// Plantilla mensaje functionalExcpetion
	public static final String MSG_EXCEPTION_FUNCTIONAL = "Functional error: {}";

	public static final String MSG_EXCEPTION = "Error: {}";

	public static final String MSG_EXEPTION_GENERAL = "An unexpected error occurred. Please contact support";

	public static final String CODE_MSG_GENERAL = "GEN_SEGUSE";

	public final static String ILEGAL_ARGUMENT_EXCEPTION = "IllegalArgumentException:";

	public final static String ILEGAL_ACCESS_EXCEPTION = "IllegalAccessException:";

}
