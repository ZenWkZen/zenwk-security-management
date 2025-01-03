package com.alineumsoft.zenwk.security.user.message.component;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class MessageSourceAccessor
 */
@Component
public class MessageSourceAccessorComponent {

	private static MessageSource messageSource;

	public MessageSourceAccessorComponent(MessageSource messageSource) {
		MessageSourceAccessorComponent.messageSource = messageSource;
	}

	public static String getMessage(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}
}
