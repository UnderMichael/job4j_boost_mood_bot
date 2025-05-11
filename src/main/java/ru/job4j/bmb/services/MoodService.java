package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.AchievementRepository;
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
		private final RecommendationEngine recommendationEngine;
		private final UserRepository userRepository;
		private final AchievementRepository achievementRepository;
		private final DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("dd-MM-yyyy HH:mm")
				.withZone(ZoneId.systemDefault());

		public MoodService(MoodRepository moodRepository, RecommendationEngine recommendationEngine,
		                   UserRepository userRepository, AchievementRepository achievementRepository) {
				this.moodRepository = moodRepository;
				this.recommendationEngine = recommendationEngine;
				this.userRepository = userRepository;
				this.achievementRepository = achievementRepository;
		}

		public Content chooseMood(User user, Long moodId) {
				return recommendationEngine.recommendFor(user.getChatId(), moodId);
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
