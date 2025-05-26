package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class MoodContentFakeRepository extends CrudRepositoryFake<MoodContent, Long> implements MoodContentRepository {
		@Override
		public List<MoodContent> findAll() {
				return memory.values().stream().toList();
		}

		@Override
		public <S extends MoodContent> Iterable<S> saveAll(Iterable<S> entities) {
				entities.forEach(this::save);
				return entities;
		}

		@Override
		public <S extends MoodContent> S save(S moodContent) {
				memory.put(moodContent.getId(), moodContent);
				return moodContent;
		}

		public Optional<MoodContent> findByMoodId(Long moodId) {
				return Optional.of(memory.get(moodId));
		}
}
