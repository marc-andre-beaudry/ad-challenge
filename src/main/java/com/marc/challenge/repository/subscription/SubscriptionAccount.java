package com.marc.challenge.repository.subscription;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marc.challenge.domain.appdirect.Notification;
import com.marc.challenge.repository.user.User;

@Entity
@Table(name = "subscription_account")
public class SubscriptionAccount {

	@Id
	@GeneratedValue
	private int id;

	private String companyName;
	private String companyUuid;
	private String edition;
	private String status;
	private String marketPlaceBaseUrl;
	private String marketPlacePartner;

	@JsonIgnore
	@OneToMany(mappedBy = "subscriptionAccount")
	private Set<User> users = new HashSet<>();

	public static SubscriptionAccount newSubscriptionFromNotification(Notification notification) {
		SubscriptionAccount account = new SubscriptionAccount();
		account.setCompanyName(notification.getPayload().getCompany().getName());
		account.setCompanyUuid(notification.getPayload().getCompany().getUuid());
		account.setMarketPlaceBaseUrl(notification.getMarketplace().getBaseUrl());
		account.setMarketPlacePartner(notification.getMarketplace().getPartner());
		account.setEdition(notification.getPayload().getOrder().getEditionCode());
		account.setStatus("ACTIVE");
		return account;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMarketPlaceBaseUrl() {
		return marketPlaceBaseUrl;
	}

	public void setMarketPlaceBaseUrl(String marketPlaceBaseUrl) {
		this.marketPlaceBaseUrl = marketPlaceBaseUrl;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getMarketPlacePartner() {
		return marketPlacePartner;
	}

	public void setMarketPlacePartner(String marketPlacePartner) {
		this.marketPlacePartner = marketPlacePartner;
	}
}
