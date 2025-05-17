package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Setting;
import ru.job4j.bmb.repository.SettingRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class SettingsFakeRepository extends CrudRepositoryFake<Setting, Long> implements SettingRepository {
		@Override
		public Optional<Setting> findByCommand(String command) {
				return Optional.empty();
		}

		@Override
		public List<Setting> findAll() {
				return List.of();
		}
}
