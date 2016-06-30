package com.marc.challenge.service;

import com.marc.challenge.controller.appdirect.response.SubscriptionResponse;
import com.marc.challenge.domain.appdirect.Notification;

public interface NotificationHandlerService {

	public SubscriptionResponse handleCreate(Notification notification);

	public SubscriptionResponse handleCancel(Notification notification);

	public SubscriptionResponse handleChange(Notification notification);

	public SubscriptionResponse handleStatus(Notification notification);

	public SubscriptionResponse handleUserAssigned(Notification notification);

	public SubscriptionResponse handleUserUnassigned(Notification notification);
}
