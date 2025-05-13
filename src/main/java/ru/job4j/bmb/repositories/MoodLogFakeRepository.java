package ru.job4j.bmb.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Profile("test")
@Repository
public class MoodLogFakeRepository
		extends CrudRepositoryFake<MoodLog, Long>
		implements MoodLogRepository {

		public List<MoodLog> findAll() {
				return new ArrayList<>(memory.values());
		}

		@Override
		public List<User> findUsersWhoDidNotVoteToday(long startOfDay) {
				return memory.values().stream()
						.filter(moodLog -> moodLog.getCreatedAt() <= startOfDay)
						.map(MoodLog::getUser)
						.distinct()
						.collect(Collectors.toList());
		}

		@Override
		public List<MoodLog> findByUser(User user) {
				return memory.values().stream()
						.filter(moodLog -> moodLog.getUser().equals(user))
						.toList();
		}

		@Override
		public <S extends MoodLog> S save(S moodLog) {
				memory.put(moodLog.getId(), moodLog);
				return moodLog;
		}

		@Override
		public <S extends MoodLog> Iterable<S> saveAll(Iterable<S> entities) {
				entities.forEach(this::save);
				return entities;
		}

}
