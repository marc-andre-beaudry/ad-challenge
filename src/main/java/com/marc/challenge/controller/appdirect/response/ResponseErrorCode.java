package com.marc.challenge.controller.appdirect.response;

// More details @ https://docs.appdirect.com/developer/billing/event-notifications/subscription-events#error-codes
public enum ResponseErrorCode {
	USER_ALREADY_EXISTS, 
	USER_NOT_FOUND,
	ACCOUNT_NOT_FOUND, 
	MAX_USERS_REACHED, 
	UNAUTHORIZED, 
	OPERATION_CANCELED,
	CONFIGURATION_ERROR, 
	INVALID_RESPONSE,
	PENDING,
	FORBIDDEN,
	BINDING_NOT_FOUND,
	TRANSPORT_ERROR,
	UNKNOWN_ERROR
}
