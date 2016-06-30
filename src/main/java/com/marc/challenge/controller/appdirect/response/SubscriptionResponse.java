package com.marc.challenge.controller.appdirect.response;

public class SubscriptionResponse {

	private final String success;

	public SubscriptionResponse(Boolean success) {
		this.success = success.toString();
	}

	public String getSuccess() {
		return success;
	}
}
