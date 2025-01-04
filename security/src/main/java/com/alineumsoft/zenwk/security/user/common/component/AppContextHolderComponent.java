package com.alineumsoft.zenwk.security.user.common.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class AppContextHolderComponent
 */
@Component
public class AppContextHolderComponent implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param applicationContext
	 * @throws BeansException
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * <p>
	 * <b> General </b> Recupera un bean a partir de un contexto
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}
}