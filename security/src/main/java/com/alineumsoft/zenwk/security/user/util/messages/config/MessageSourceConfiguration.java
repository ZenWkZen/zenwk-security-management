package com.alineumsoft.zenwk.security.user.util.messages.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.alineumsoft.zenwk.security.user.util.constants.ConfigUtilConstants;

@Configuration
public class MessageSourceConfiguration {

	@Value("${app.default.locale:es}")
	private String dafaultLocale;

	/**
	 * <p>
	 * <b> Util </b> Se configura el messageSource para que soporte la
	 * internacionalizacion
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(ConfigUtilConstants.CLASSPATH_LANG);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(3600);
		return messageSource;
	}

	/**
	 * <p>
	 * <b> Util </b> Carga el lenguaje por defecto indicado en el archivo de
	 * configuracion en caso que no este carga el lengaje por defecto ES
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(Locale.forLanguageTag(dafaultLocale));
		return localeResolver;
	}

}