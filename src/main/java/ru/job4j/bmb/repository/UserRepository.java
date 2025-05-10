package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
		List<User> findAll();

		public void add(User user);

		User findByClientId(Long clientId);
}
