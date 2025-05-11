package ru.job4j.bmb.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.AchievementRepository;
import ru.job4j.bmb.repositories.MoodLogRepository;
import ru.job4j.bmb.repositories.MoodRepository;
import ru.job4j.bmb.repositories.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

		public Content chooseMood(User user, Long moodId) {
				var content = recommendationEngine.recommendFor(user.getChatId(), moodId);
				moodRepository.findById(moodId)
						.ifPresent(mood -> moodLogRepository.save(
								new MoodLog()
										.setUser(user)
										.setMood(mood)));
				publisher.publishEvent(new UserEvent(this, user));
				return content;
		}

		public Optional<Content> weekMoodLogCommand(Long chatId, Long clientId) {
				var content = new Content(chatId);
				return Optional.of(content);
		}

		public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
				var content = new Content(chatId);
				return Optional.of(content);
		}

		private String formatMoodLogs(List<MoodLog> logs, String title) {
				if (logs.isEmpty()) {
						return title + ":\nNo mood logs found.";
				}
				var sb = new StringBuilder();
				logs.forEach(log -> {
						String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
						sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
				});
				return sb.toString();
		}

		public Optional<Content> awards(long chatId, Long clientId) {
				var content = new Content(chatId);
				return Optional.of(content);
		}
}
