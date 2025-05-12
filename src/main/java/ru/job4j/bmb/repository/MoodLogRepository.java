package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
		@Override
		List<MoodLog> findAll();

		@Query("SELECT u "
				+ "FROM User u "
				+ "LEFT JOIN MoodLog ml ON ml.user = u AND ml.createdAt >= :startOfDay "
				+ "WHERE ml.id IS NULL ")
		List<User> findUsersWhoDidNotVoteToday(@Param("startOfDay") long startOfDay);

		List<MoodLog> findByUser(User user);
}
