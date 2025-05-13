package ru.job4j.bmb.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodService {
		private final MoodRepository moodRepository;
		private final MoodLogRepository moodLogRepository;
		private final RecommendationEngine recommendationEngine;
		private final UserRepository userRepository;
		private final AchievementRepository achievementRepository;
		private final ApplicationEventPublisher publisher;
		private final DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("dd-MM-yyyy HH:mm")
				.withZone(ZoneId.systemDefault());

		public MoodService(MoodRepository moodRepository, MoodLogRepository moodLogRepository, RecommendationEngine recommendationEngine,
		                   UserRepository userRepository, AchievementRepository achievementRepository,
		                   ApplicationEventPublisher publisher) {
				this.moodRepository = moodRepository;
				this.moodLogRepository = moodLogRepository;
				this.recommendationEngine = recommendationEngine;
				this.userRepository = userRepository;
				this.achievementRepository = achievementRepository;
				this.publisher = publisher;
		}

		private Optional<Content> getLogsForDiapason(long diapason, String title, Long chatId) {
				var moodLogs = userRepository.findByChatId(chatId).stream()
						.flatMap(user -> moodLogRepository.findByUser(user).stream())
						.peek(log -> System.out.println(log.getMood()))
						.sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
						.takeWhile(log -> log.getCreatedAt() >= diapason)
						.toList();
				return Optional.of(
						new Content(chatId)
								.setText(formatMoodLogs(moodLogs, title))
				);
		}

		public Content chooseMood(User user, Long moodId) {
				var content = recommendationEngine.recommendFor(user.getChatId(), moodId);
				moodRepository.findById(moodId)
						.ifPresent(mood -> moodLogRepository.save(
								new MoodLog()
										.setCreatedAt()
										.setUser(user)
										.setMood(mood)));
				publisher.publishEvent(new UserEvent(this, user));
				return content;
		}

		public Optional<Content> weekMoodLogCommand(Long chatId, Long clientId) {
				long oneWeekAgo = Instant.now()
						.atZone(ZoneId.systemDefault())
						.minusDays(7)
						.toInstant()
						.toEpochMilli();
				return getLogsForDiapason(oneWeekAgo, "Логи за прошедшую неделю", chatId);
		}

		public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
				long oneMonthAgo = Instant.now()
						.atZone(ZoneId.systemDefault())
						.minusMonths(7)
						.toInstant()
						.toEpochMilli();
				return getLogsForDiapason(oneMonthAgo, "Логи за прошедший месяц", chatId);
		}

		private String formatMoodLogs(List<MoodLog> logs, String title) {
				if (logs.isEmpty()) {
						return title + ":\nNo mood logs found.";
				}
				var sb = new StringBuilder().append(title).append(":").append("\n\n");
				logs.forEach(log -> {
						String formattedDate = formatter.format(Instant.ofEpochMilli(log.getCreatedAt()));
						sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
				});
				return sb.toString();
		}

		public Optional<Content> awards(long chatId, Long clientId) {
				System.out.println(chatId);
				var awards = userRepository.findByChatId(chatId).stream()
						.flatMap(user -> achievementRepository.findByUser(user).stream())
						.map(achievement -> achievement.getAward().getTitle() + ": " + achievement.getAward().getDescription())
						.collect(Collectors.joining("\n\n"));
				return Optional.of(
						new Content(chatId)
								.setText(awards));
		}
}
