package com.alineumsoft.zenwk.security.verification.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * <p>
 * Generador de códigos
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class codeGenerator
 */
public class CodeGenerator {
	/**
	 * random
	 */
	private static SecureRandom random = new SecureRandom();

	/**
	 * 
	 * <p>
	 * <b> CU003_Gestionar token de verificación. </b> Generador.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param size
	 * @return
	 */
	public static String generateCode(int size) {
		byte[] randomBytes = new byte[size];
		random.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, size);
	}

}
