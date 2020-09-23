package org.paasta.container.platform.api.exception;

public class ContainerPlatformException extends BaseBizException {
	private static final long serialVersionUID = -1288712633779609678L;

	public ContainerPlatformException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public ContainerPlatformException(String errorCode, String errorMessage, int statusCode, String detailMessage) {
		super(errorCode, errorMessage, statusCode, detailMessage);
	}

	public ContainerPlatformException(String errorMessage) {
		super(errorMessage);
	}
}
