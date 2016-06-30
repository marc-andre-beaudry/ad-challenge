package com.marc.challenge.repository.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.repository.subscription.SubscriptionAccount;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	private int id;

	private String openId;
	private String firstName;
	private String lastName;
	private String email;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriptionAccountId")
	private SubscriptionAccount subscriptionAccount;

	public static User createNewFromNotificationCreator(Notification notification) {
		User user = new User();
		user.setOpenId(notification.getCreator().getOpenId());
		user.setFirstName(notification.getCreator().getFirstName());
		user.setLastName(notification.getCreator().getLastName());
		user.setEmail(notification.getCreator().getEmail());
		return user;
	}

	public static User createNewFromNotificationPayloadUser(Notification notification) {
		User user = new User();
		user.setOpenId(notification.getPayload().getUser().getOpenId());
		user.setFirstName(notification.getPayload().getUser().getFirstName());
		user.setLastName(notification.getPayload().getUser().getLastName());
		user.setEmail(notification.getPayload().getUser().getEmail());
		return user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SubscriptionAccount getSubscriptionAccount() {
		return subscriptionAccount;
	}

	public void setSubscriptionAccount(SubscriptionAccount subscriptionAccount) {
		this.subscriptionAccount = subscriptionAccount;
	}
}
