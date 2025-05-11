package ru.job4j.bmb.repositories;

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

		@Query("select u from User u where not exists (select ml from MoodLog ml where ml.user = u and ml.createdAt >= :startOfDay )")
		List<User> findUsersWhoDidNotVoteToday(@Param("startOfDay") long startOfDay);

		List<MoodLog> findByUser(User user);
}
