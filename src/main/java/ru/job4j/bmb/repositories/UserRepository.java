package ru.job4j.bmb.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
		@Override
		List<User> findAll();
}
