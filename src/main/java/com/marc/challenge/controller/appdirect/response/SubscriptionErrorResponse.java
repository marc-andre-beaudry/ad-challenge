package com.marc.challenge.controller.appdirect.response;

public class SubscriptionErrorResponse extends SubscriptionResponse {

	private final ResponseErrorCode errorCode;
	private final String message;

	public SubscriptionErrorResponse() {
		this(ResponseErrorCode.UNKNOWN_ERROR, "Unknown server error");
	}

	public SubscriptionErrorResponse(ResponseErrorCode errorCode, String message) {
		super(false);
		this.errorCode = errorCode;
		this.message = message;
	}

	public ResponseErrorCode getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
}
