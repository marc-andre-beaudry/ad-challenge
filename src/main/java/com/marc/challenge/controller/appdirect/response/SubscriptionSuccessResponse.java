package com.marc.challenge.controller.appdirect.response;

public class SubscriptionSuccessResponse extends SubscriptionResponse {

	private final String accountIdentifier;

	public SubscriptionSuccessResponse(String accountIdentifier) {
		super(true);
		this.accountIdentifier = accountIdentifier;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}
}
