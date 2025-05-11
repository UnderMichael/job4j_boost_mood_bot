package ru.job4j.bmb.repositories;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

}
