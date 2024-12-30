package com.alineumsoft.zenwk.security.user.messages.enums;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class MessageKeyEnum
 */
public enum MessageKeyEnum {
	USER_CREATION_EXIT("user.creation.exit"),
	USER_CREATION_ERROR("user.creation.error"),
	DATA_UPDATE_NULL("util.data.error.update.null"),
	DATA_UPDATE_FAILED("util.data.error.udpate.failed");

	private final String key;

	MessageKeyEnum(String key) {
		this.key = key;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return key;
	}

}
