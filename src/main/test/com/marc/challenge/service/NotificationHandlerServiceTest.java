package com.marc.challenge.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marc.challenge.Application;
import com.marc.challenge.controller.appdirect.response.SubscriptionResponse;
import com.marc.challenge.controller.appdirect.response.SubscriptionSuccessResponse;
import com.marc.challenge.domain.appdirect.Account;
import com.marc.challenge.domain.appdirect.Company;
import com.marc.challenge.domain.appdirect.Creator;
import com.marc.challenge.domain.appdirect.Marketplace;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.domain.appdirect.NotificationFlag;
import com.marc.challenge.domain.appdirect.NotificationType;
import com.marc.challenge.domain.appdirect.Order;
import com.marc.challenge.domain.appdirect.Payload;
import com.marc.challenge.repository.subscription.SubscriptionAccount;
import com.marc.challenge.repository.subscription.SubscriptionAccountRepository;
import com.marc.challenge.repository.user.User;
import com.marc.challenge.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@TestPropertySource(locations = "classpath:com/marc/challenge/service/test.properties")
public class NotificationHandlerServiceTest {

	private NotificationHandlerService notificationHandlerService;

	// Normally would do unit tests with a mock, then do integration test with
	// in-memory user & account repository.
	// Limited time, we have H2 and this is a proof of concept, so...
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubscriptionAccountRepository subscriptionAccountRepository;

	@Before
	public void setUp() throws Exception {
		userRepository.deleteAllInBatch();
		subscriptionAccountRepository.deleteAllInBatch();
		notificationHandlerService = new NotificationHandlerServiceImpl(userRepository, subscriptionAccountRepository);
	}

	@Test
	public void test_that_ping_on_create_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleCreate(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_cancel_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleCancel(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_change_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleChange(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_status_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleStatus(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_assign_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleUserAssigned(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_ping_on_unassign_returns_success() {
		Notification notification = buildPingNotification();
		SubscriptionResponse response = notificationHandlerService.handleUserUnassigned(notification);
		Assert.assertEquals("true", response.getSuccess());
	}

	@Test
	public void test_that_create_notification_generate_user_and_account_properly() {
		Notification notification = buildSubscriptionOrderNotification();
		SubscriptionResponse response = notificationHandlerService.handleCreate(notification);

		Optional<User> user = userRepository.findAll().stream().findFirst();
		Optional<SubscriptionAccount> account = subscriptionAccountRepository.findAll().stream().findFirst();

		Assert.assertEquals("Expecting success true", "true", response.getSuccess());
		Assert.assertEquals("Expecting a SubscriptionSuccessResponse", SubscriptionSuccessResponse.class,
				response.getClass());
		Assert.assertEquals("Expecting a user to be present in the repository", true, user.isPresent());
		Assert.assertEquals("Expecting an account to be present in the repository", true, account.isPresent());
		Assert.assertEquals("Expecting returned account id to be the same as reponsitory account id",
				String.valueOf(account.get().getId()), ((SubscriptionSuccessResponse) response).getAccountIdentifier());

	}

	@Test
	public void test_that_change_notification_modify_account_properly() {
		Notification createNotification = buildSubscriptionOrderNotification();
		SubscriptionResponse response = notificationHandlerService.handleCreate(createNotification);

		SubscriptionSuccessResponse successResponse = (SubscriptionSuccessResponse) response;
		Notification changeNotification = buildSubscriptionChangeNotification(successResponse.getAccountIdentifier());
		notificationHandlerService.handleChange(changeNotification);

		Optional<SubscriptionAccount> account = subscriptionAccountRepository.findAll().stream().findFirst();
		Assert.assertEquals("Edition should now be recurring", "RECURRING", account.get().getEdition());
	}

	private Notification buildPingNotification() {
		Notification notification = new Notification();
		notification.setFlag(NotificationFlag.STATELESS);
		return notification;
	}

	private Notification buildSubscriptionOrderNotification() {
		Notification notification = new Notification();
		notification.setApplicationUuid("aaaaaa");
		notification.setFlag(NotificationFlag.DEVELOPMENT);
		notification.setCreator(buildCreator());
		notification.setType(NotificationType.SUBSCRIPTION_ORDER);
		notification.setMarketplace(buildMarketplace());

		Payload payload = new Payload();
		payload.setCompany(buildCompany());
		payload.setOrder(buildOrder("FREE"));
		notification.setPayload(payload);
		return notification;
	}

	private Notification buildSubscriptionChangeNotification(String accountId) {
		Notification notification = new Notification();
		notification.setApplicationUuid("aaaaaa");
		notification.setFlag(NotificationFlag.DEVELOPMENT);
		notification.setCreator(buildCreator());
		notification.setType(NotificationType.SUBSCRIPTION_CHANGE);
		notification.setMarketplace(buildMarketplace());

		Payload payload = new Payload();
		payload.setAccount(buildAccount(accountId));
		payload.setOrder(buildOrder("RECURRING"));
		notification.setPayload(payload);
		return notification;
	}

	private Account buildAccount(String accountId) {
		Account account = new Account();
		account.setAccountIdentifier(accountId);
		account.setStatus("ACTIVE");
		return account;
	}

	private Notification buildSubscriptionCancelNotification() {
		Notification notification = new Notification();
		notification.setApplicationUuid("aaaaaa");
		notification.setFlag(NotificationFlag.DEVELOPMENT);
		notification.setCreator(buildCreator());
		notification.setType(NotificationType.SUBSCRIPTION_CHANGE);
		notification.setMarketplace(buildMarketplace());

		Payload payload = new Payload();
		payload.setCompany(buildCompany());
		payload.setOrder(buildOrder("RECURRING"));
		notification.setPayload(payload);
		return notification;
	}

	private Order buildOrder(String editionCode) {
		Order order = new Order();
		order.setEditionCode(editionCode);
		order.setPricingDuration("MONTHLY");
		return order;
	}

	private Marketplace buildMarketplace() {
		Marketplace marketplace = new Marketplace();
		marketplace.setBaseUrl("https://marc-andre-beaudry.com/bla");
		marketplace.setPartner("marcandre");
		return marketplace;
	}

	private Company buildCompany() {
		Company company = new Company();
		company.setCountry("CA");
		company.setName("Sample company inc.");
		company.setUuid("qwerty");
		company.setPhoneNumber("123-456-7890");
		return company;
	}

	private Creator buildCreator() {
		Creator creator = new Creator();
		creator.setOpenId("https://www.test.com/openid/id/abcd");
		creator.setUuid("abcd");
		creator.setFirstName("Marc-Andre");
		creator.setLastName("Beaudry");
		creator.setEmail("sample.email@gmail.com");
		return creator;
	}
}
