package com.alineumsoft.zenwk.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Redirigir las peticiones http a seguras|
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class WebConfig
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param registry
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/secure-endpoint");
	}

}
