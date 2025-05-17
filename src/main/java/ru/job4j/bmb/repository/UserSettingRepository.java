package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.model.UserSetting;

import java.util.List;

@Repository
public interface UserSettingRepository extends CrudRepository<UserSetting, Long> {
		@Override
		List<UserSetting> findAll();

		List<UserSetting> findByUser(User user);

}
