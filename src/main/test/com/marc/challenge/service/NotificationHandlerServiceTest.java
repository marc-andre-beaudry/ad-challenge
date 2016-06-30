package com.marc.challenge.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marc.challenge.Application;
import com.marc.challenge.controller.appdirect.response.SubscriptionResponse;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.domain.appdirect.NotificationFlag;
import com.marc.challenge.repository.subscription.SubscriptionAccountRepository;
import com.marc.challenge.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@TestPropertySource(locations = "classpath:application.properties")
public class NotificationHandlerServiceTest {

	private NotificationHandlerService notificationHandlerService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SubscriptionAccountRepository subscriptionAccountRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		notificationHandlerService = new NotificationHandlerServiceImpl(userRepository, subscriptionAccountRepository);
	}

	@Test
	public void test_that_ping_on_create_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleCreate(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_cancel_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleCancel(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_change_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleChange(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_status_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleStatus(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_assign_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleUserAssigned(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_unassign_returns_success() {
		Notification notification = createPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleUserUnassigned(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	private Notification createPingNotification() {
		Notification notification = new Notification();
		notification.setFlag(NotificationFlag.STATELESS);
		return notification;
	}
}
