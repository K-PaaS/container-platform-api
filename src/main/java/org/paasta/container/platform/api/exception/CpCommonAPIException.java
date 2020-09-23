package org.paasta.container.platform.api.exception;

public class CpCommonAPIException extends BaseBizException {
	private static final long serialVersionUID = -1288712633779609678L;

	public CpCommonAPIException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public CpCommonAPIException(String errorCode, String errorMessage, int statusCode, String detailMessage) {
		super(errorCode, errorMessage, statusCode, detailMessage);
	}

	public CpCommonAPIException(String errorMessage) {
		super(errorMessage);
	}
}
