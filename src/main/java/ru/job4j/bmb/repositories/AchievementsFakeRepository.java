package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AchievementsFakeRepository extends CrudRepositoryFake<Achievement, Long> implements AchievementRepository {
		@Override
		public List<Achievement> findAll() {
				return new ArrayList<>(memory.values());
		}

		@Override
		public <S extends Achievement> Iterable<S> saveAll(Iterable<S> entities) {
				entities.forEach(achievement -> memory.put(achievement.getId(), achievement));
				return entities;
		}

		@Override
		public List<Achievement> findByUser(User user) {
				return memory.values().stream()
						.filter(achievement -> achievement.getUser().equals(user))
						.toList();
		}
}
