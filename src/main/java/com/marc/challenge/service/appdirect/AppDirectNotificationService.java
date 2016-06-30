package com.marc.challenge.service.appdirect;

import com.marc.challenge.domain.appdirect.Notification;

public interface AppDirectNotificationService {

	public Notification getNotificationForUrl(String url);
}
