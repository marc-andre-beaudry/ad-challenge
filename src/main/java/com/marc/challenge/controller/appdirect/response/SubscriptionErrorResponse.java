package com.marc.challenge.controller.appdirect.response;

public class SubscriptionErrorResponse extends SubscriptionResponse {

	private final String errorCode;
	private final String message;

	public SubscriptionErrorResponse() {
		this("UNKOWN_ERROR", "Unknown server error");
	}

	public SubscriptionErrorResponse(String errorCode, String message) {
		super(false);
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
}
