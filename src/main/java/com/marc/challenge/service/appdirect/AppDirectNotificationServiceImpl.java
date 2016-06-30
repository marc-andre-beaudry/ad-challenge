package com.marc.challenge.service.appdirect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.marc.challenge.domain.appdirect.Notification;

@Service
public class AppDirectNotificationServiceImpl implements AppDirectNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(AppDirectNotificationServiceImpl.class);

	private final OAuthRestTemplate template;

	@Autowired
	public AppDirectNotificationServiceImpl(ProtectedResourceDetails protectedResourceDetails) {
		this.template = new OAuthRestTemplate(protectedResourceDetails);
	}

	@Override
	public Notification getNotificationForUrl(String url) {
		Notification event = null;
		try {
			event = template.getForObject(url, Notification.class);
			if (logger.isDebugEnabled()) {
				Gson gson = new Gson();
				String eventAsString = gson.toJson(event);
				logger.info("event:" + eventAsString);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return event;
	}
}
