package com.alineumsoft.zenwk.security.user.util.messages.enums;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class MessageKeyEnum
 */
public enum MessageKeyUtilEnum {
	DATA_UPDATE_NULL("util.data.error.update.null"),
	DATA_UPDATE_FAILED("util.data.error.udpate.failed");

	private final String key;

	private MessageKeyUtilEnum(String key) {
		this.key = key;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}
}
