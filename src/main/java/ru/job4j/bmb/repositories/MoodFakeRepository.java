package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MoodFakeRepository extends CrudRepositoryFake<Mood, Long> implements MoodRepository {
		@Override
		public List<Mood> findAll() {
				return new ArrayList<>(memory.values());
		}

		@Override
		public <S extends Mood> Iterable<S> saveAll(Iterable<S> entities) {
				entities.forEach(mood -> memory.put(mood.getId(), mood));
				return entities;
		}
}
