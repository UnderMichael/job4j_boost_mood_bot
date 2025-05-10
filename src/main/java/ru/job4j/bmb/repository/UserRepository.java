package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
