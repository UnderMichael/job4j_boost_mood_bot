package ru.job4j.bmb.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
		@Override
		List<MoodLog> findAll();

		public List<MoodLog> findByUserId(Long userId);

		public Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

		public List<User> findUsersWhoDidNotVoteToday(long startOfDay);

		public List<MoodLog> findMoodLogsForWeek(Long userId, long weekStart);

		public List<MoodLog> findMoodLogsForMonth(Long userId, long monthStart);
}
