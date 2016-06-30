package com.marc.challenge.controller.appdirect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marc.challenge.controller.appdirect.response.SubscriptionErrorResponse;
import com.marc.challenge.controller.appdirect.response.SubscriptionResponse;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.service.NotificationHandlerService;
import com.marc.challenge.service.appdirect.AppDirectNotificationService;

@RestController
@RequestMapping("/api/v1/subscription/notification")
public class SubscriptionRestController {

	private static final Logger logger = LoggerFactory.getLogger(SubscriptionRestController.class);

	private final AppDirectNotificationService appDirectNotificationService;

	private final NotificationHandlerService notificationHandlerService;

	@Autowired
	public SubscriptionRestController(AppDirectNotificationService appDirectNotificationService,
			NotificationHandlerService notificationHandlerService) {
		this.appDirectNotificationService = appDirectNotificationService;
		this.notificationHandlerService = notificationHandlerService;
	}

	@RequestMapping("create")
	public SubscriptionResponse create(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleCreate(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}

	@RequestMapping("cancel")
	public SubscriptionResponse cancel(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleCancel(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}

	@RequestMapping("change")
	public SubscriptionResponse change(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleChange(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}

	@RequestMapping("user/assign")
	public SubscriptionResponse assign(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleUserAssigned(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}

	@RequestMapping("user/unassign")
	public SubscriptionResponse unassign(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleUserUnassigned(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}

	@RequestMapping("status")
	public SubscriptionResponse status(@RequestParam(value = "url", required = false) String url) {
		if (url == null) {
			return new SubscriptionErrorResponse();
		}
		try {
			Notification notification = appDirectNotificationService.getNotificationForUrl(url);
			return notificationHandlerService.handleStatus(notification);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new SubscriptionErrorResponse();
	}
}
