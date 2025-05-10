package ru.job4j.bmb.repository;

import ru.job4j.bmb.model.User;

import java.util.List;

public interface UserRepository {
		List<User> findAll();

		public void add(User user);

		User findByClientId(Long clientId);
}
