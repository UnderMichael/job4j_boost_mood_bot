package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Setting;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends CrudRepository<Setting, Long> {
		@Override
		List<Setting> findAll();

		Optional<Setting> findByCommand(String command);
}
