package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.model.UserSetting;
import ru.job4j.bmb.repository.UserSettingRepository;

import java.util.List;

@Repository
public class UserSettingFakeRepository extends CrudRepositoryFake<UserSetting, Long> implements UserSettingRepository {
		@Override
		public List<UserSetting> findByUser(User user) {
				return List.of();
		}

		@Override
		public List<UserSetting> findAll() {
				return List.of();
		}
}
