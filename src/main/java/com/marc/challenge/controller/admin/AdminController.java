package com.marc.challenge.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.marc.challenge.repository.subscription.SubscriptionAccount;
import com.marc.challenge.repository.subscription.SubscriptionAccountRepository;
import com.marc.challenge.repository.user.User;
import com.marc.challenge.repository.user.UserRepository;

@RestController
@RequestMapping("/admin/v1")
public class AdminController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	private final UserRepository userRepository;
	private final SubscriptionAccountRepository subscriptionAccountRepository;

	@Autowired
	public AdminController(UserRepository userRepository, SubscriptionAccountRepository subscriptionAccountRepository) {
		this.userRepository = userRepository;
		this.subscriptionAccountRepository = subscriptionAccountRepository;
	}

	@RequestMapping(value = "/user", method = { RequestMethod.GET })
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@RequestMapping(value = "/user/{id}", method = { RequestMethod.GET })
	public User getUser(@PathVariable(value = "id") Integer id) {
		return userRepository.findOne(id);
	}

	@RequestMapping(value = "/user/{id}/subscription", method = { RequestMethod.GET })
	public SubscriptionAccount getSubscriptionAccountsForUser(@PathVariable(value = "id") Integer id) {
		if (userRepository.exists(id)) {
			SubscriptionAccount subscriptionAccount = userRepository.findOne(id).getSubscriptionAccount();
			return subscriptionAccount;
		} else {
			throw new EntityNotFoundException();
		}
	}

	@RequestMapping(value = "/subscription", method = { RequestMethod.GET })
	public List<SubscriptionAccount> getSubscriptionAccounts() {
		return subscriptionAccountRepository.findAll();
	}

	@RequestMapping(value = "/subscription/{id}/user", method = { RequestMethod.GET })
	public List<User> getUsersForSubscriptionAccount(@PathVariable(value = "id") Integer id) {
		if (subscriptionAccountRepository.exists(id)) {
			Set<User> users = subscriptionAccountRepository.findOne(id).getUsers();
			return users.stream().collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}
}
