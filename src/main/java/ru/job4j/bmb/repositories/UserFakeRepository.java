package ru.job4j.bmb.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserFakeRepository implements UserRepository {
		private final Map<Long, User> userMap = new HashMap<>();

		@Override
		public User save(User user) {
				userMap.put(user.getClientId(), user);
				return user;
		}

		@Override
		public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
				return null;
		}

		@Override
		public Optional<User> findById(Long clientId) {
				return Optional.of(userMap.get(clientId));
		}

		@Override
		public boolean existsById(Long aLong) {
				return false;
		}

		@Override
		public List<User> findAll() {
				return userMap.values().stream().toList();
		}

		@Override
		public Iterable<User> findAllById(Iterable<Long> longs) {
				return null;
		}

		@Override
		public long count() {
				return 0;
		}

		@Override
		public void deleteById(Long aLong) {

		}

		@Override
		public void delete(User entity) {

		}

		@Override
		public void deleteAllById(Iterable<? extends Long> longs) {

		}

		@Override
		public void deleteAll(Iterable<? extends User> entities) {

		}

		@Override
		public void deleteAll() {

		}

		@Override
		public Optional<User> findByChatId(long chatId) {
				return userMap.values().stream()
						.filter(user -> user.getChatId() == chatId)
						.findFirst();
		}
}
