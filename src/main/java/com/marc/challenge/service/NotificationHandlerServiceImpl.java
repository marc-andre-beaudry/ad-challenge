package com.marc.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marc.challenge.controller.appdirect.response.ResponseErrorCode;
import com.marc.challenge.controller.appdirect.response.SubscriptionErrorResponse;
import com.marc.challenge.controller.appdirect.response.SubscriptionResponse;
import com.marc.challenge.controller.appdirect.response.SubscriptionSuccessResponse;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.domain.appdirect.NotificationFlag;
import com.marc.challenge.repository.subscription.SubscriptionAccount;
import com.marc.challenge.repository.subscription.SubscriptionAccountRepository;
import com.marc.challenge.repository.user.User;
import com.marc.challenge.repository.user.UserRepository;

@Service
public class NotificationHandlerServiceImpl implements NotificationHandlerService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationHandlerServiceImpl.class);

	private final UserRepository userRepository;
	private final SubscriptionAccountRepository subscriptionAccountRepository;

	@Autowired
	public NotificationHandlerServiceImpl(UserRepository userRepository,
			SubscriptionAccountRepository subscriptionAccountRepository) {
		this.userRepository = userRepository;
		this.subscriptionAccountRepository = subscriptionAccountRepository;
	}

	// https://docs.appdirect.com/developer/billing/event-notifications/user-events#event-flags
	// Event flag 'STATELESS' is handled correctly for api uptime monitoring
	@Override
	public SubscriptionResponse handleCreate(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}

		// TODO OneToMany or ManyToMany ?
		SubscriptionAccount subscriptionAccount = SubscriptionAccount.newSubscriptionFromNotification(notification);
		subscriptionAccount = subscriptionAccountRepository.saveAndFlush(subscriptionAccount);
		User creatorUser = User.createNewFromNotificationCreator(notification);
		userRepository.saveAndFlush(creatorUser);
		creatorUser.setSubscriptionAccount(subscriptionAccount);
		return new SubscriptionSuccessResponse(String.valueOf(subscriptionAccount.getId()));
	}

	@Override
	public SubscriptionResponse handleCancel(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}

		Integer accountIdentifier = Integer.parseInt(notification.getPayload().getAccount().getAccountIdentifier());
		if (subscriptionAccountRepository.exists(accountIdentifier)) {
			SubscriptionAccount account = subscriptionAccountRepository.findOne(accountIdentifier);
			account.setStatus("INACTIVE");
			subscriptionAccountRepository.saveAndFlush(account);
		} else if (NotificationFlag.DEVELOPMENT != notification.getFlag()) {
			return new SubscriptionErrorResponse(ResponseErrorCode.ACCOUNT_NOT_FOUND, "Account not found");
		}
		return new SubscriptionResponse(true);
	}

	@Override
	public SubscriptionResponse handleChange(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}

		String accountIdentifier = notification.getPayload().getAccount().getAccountIdentifier();
		SubscriptionAccount subscriptionAccount = subscriptionAccountRepository
				.findOne(Integer.parseInt(accountIdentifier));

		if (subscriptionAccount == null) {
			return new SubscriptionErrorResponse(ResponseErrorCode.ACCOUNT_NOT_FOUND, "Account not found");
		} else {
			// TODO, Let's only deal with the edition at the moment
			subscriptionAccount.setEdition(notification.getPayload().getOrder().getEditionCode());
			subscriptionAccountRepository.saveAndFlush(subscriptionAccount);
			return new SubscriptionResponse(true);
		}
	}

	@Override
	public SubscriptionResponse handleStatus(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}

		String accountIdentifier = notification.getPayload().getAccount().getAccountIdentifier();
		SubscriptionAccount subscriptionAccount = subscriptionAccountRepository
				.findOne(Integer.parseInt(accountIdentifier));
		if (subscriptionAccount == null) {
			return new SubscriptionErrorResponse(ResponseErrorCode.ACCOUNT_NOT_FOUND, "Account not found");
		} else {
			return new SubscriptionResponse(true);
		}
	}

	@Override
	public SubscriptionResponse handleUserAssigned(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}

		Integer accountIdentifier = Integer.valueOf(notification.getPayload().getAccount().getAccountIdentifier());
		SubscriptionAccount account = subscriptionAccountRepository.getOne(accountIdentifier);

		User user = User.createNewFromNotificationPayloadUser(notification);
		user.setSubscriptionAccount(account);
		user = userRepository.saveAndFlush(user);

		return new SubscriptionResponse(true);
	}

	@Override
	public SubscriptionResponse handleUserUnassigned(Notification notification) {
		if (NotificationFlag.STATELESS.equals(notification.getFlag())) {
			logger.info("Received a notification with STATELESS flag");
			return new SubscriptionResponse(true);
		}
		Integer accountIdentifier = Integer.valueOf(notification.getPayload().getAccount().getAccountIdentifier());
		SubscriptionAccount account = subscriptionAccountRepository.getOne(accountIdentifier);
		account.getUsers().removeIf(x -> x.getOpenId().equals(notification.getPayload().getUser().getOpenId()));
		return new SubscriptionResponse(true);
	}

}
