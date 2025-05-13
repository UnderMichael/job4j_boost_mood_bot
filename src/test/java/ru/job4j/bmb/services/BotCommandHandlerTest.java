package ru.job4j.bmb.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.constants.InitialDbValues;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repositories.AchievementsFakeRepository;
import ru.job4j.bmb.repositories.MoodFakeRepository;
import ru.job4j.bmb.repositories.MoodLogFakeRepository;
import ru.job4j.bmb.repositories.UserFakeRepository;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {
		BotCommandHandler.class,
		MoodService.class,
		UserFakeRepository.class,
		MoodLogFakeRepository.class,
		AchievementsFakeRepository.class,
		RecommendationEngine.class,
		AchievementRepository.class,
		TgUI.class,
		MoodFakeRepository.class
})
class BotCommandHandlerTest {
		@Autowired
		private BotCommandHandler botCommandHandler;
		@Autowired
		private TgUI tgUI;
		@Autowired
		private RecommendationEngine recommendationEngine;
		@Autowired
		private AchievementRepository achievementRepository;
		@Autowired
		private MoodService moodService;
		@Autowired
		private MoodRepository moodRepository;
		@Autowired
		private MoodLogRepository moodLogRepository;
		@Autowired
		private UserRepository userRepository;

		@BeforeEach
		void setup() {
				userRepository.deleteAll();
				var moods = moodRepository.findAll();
				if (!moods.isEmpty()) {
						return;
				}
				moods = InitialDbValues.MOODS.stream().map(MoodContent::getMood).toList();
				List<Mood> result = new ArrayList<>();
				for (int i = 0; i < moods.size(); i++) {
						result.add(moods.get(i).setId((long) i + 1));
				}
				moodRepository.saveAll(result);
		}

		@Test
		public void whenStartCommand() {
				userRepository.save(
						new User()
								.setChatId(1)
								.setClientId(1));
				var input = new Message();
				input.setText("/start");
				input.setFrom(new org.telegram.telegrambots.meta.api.objects.User(1L, "", false));
				input.setChat(new Chat(1L, ""));
				var result = botCommandHandler.commands(input);
				var expected = Optional.of(
						new Content(1L)
								.setText("Как настроение?")
								.setMarkup(buildButtons()));
				assertThat(result).isPresent().isEqualTo(expected);
		}

		@Test
		public void whenAwardsCommand() {
				var user = new User()
						.setChatId(1)
						.setClientId(1);
				userRepository.save(user);
				achievementRepository.saveAll(List.of(
						new Achievement().setUser(user).setId(1L).setAward(InitialDbValues.AWARDS.get(0)),
						new Achievement().setUser(user).setId(2L).setAward(InitialDbValues.AWARDS.get(5)),
						new Achievement().setUser(user).setId(3L).setAward(InitialDbValues.AWARDS.get(3))
				));
				var input = new Message();
				input.setText("/award");
				input.setFrom(new org.telegram.telegrambots.meta.api.objects.User(1L, "", false));
				input.setChat(new Chat(1L, ""));
				var result = botCommandHandler.commands(input);
				var expectedText = "Смайлик дня: За 1 день хорошего настроения.\n\n"
						+ "Виртуальный подарок: После 15 дней хорошего настроения. Награда: Возможность отправить или получить виртуальный подарок внутри приложения.\n\n"
						+ "Персонализированные рекомендации: После 5 дней хорошего настроения. Награда: Подборка контента или "
						+ "активности на основе интересов пользователя.";
				var expected = Optional.of(
						new Content(1L)
								.setText(expectedText));
				assertThat(result).isPresent().isEqualTo(expected);
		}

		@Test
		public void whenWeekMoodLogCommand() {
				var user = new User()
						.setChatId(1)
						.setClientId(1);
				var currTime = DateTimeFormatter
						.ofPattern("dd-MM-yyyy HH:mm")
						.withZone(ZoneId.systemDefault())
						.format(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
				userRepository.save(user);

				List<Mood> moods = InitialDbValues.MOODS.stream().map(MoodContent::getMood).toList();
				moodLogRepository.saveAll(List.of(
						new MoodLog().setUser(user).setMood(moods.get(0)).setId(1L).setCreatedAt(),
						new MoodLog().setUser(user).setMood(moods.get(0)).setId(2L).setCreatedAt(),
						new MoodLog().setUser(user).setMood(moods.get(0)).setId(3L).setCreatedAt()));
				var input = new Message();
				input.setText("/week_mood_log");
				input.setFrom(new org.telegram.telegrambots.meta.api.objects.User(1L, "", false));
				input.setChat(new Chat(1L, ""));
				var result = botCommandHandler.commands(input);
				String expectedString = "Логи за прошедшую неделю:\n\n"
						+ currTime + ": Счастливейший на свете \uD83D\uDE0E\n"
						+ currTime + ": Счастливейший на свете \uD83D\uDE0E\n"
						+ currTime + ": Счастливейший на свете \uD83D\uDE0E\n";
				var expected = Optional.of(
						new Content(1L)
								.setText(expectedString));
				assertThat(result).isPresent().isEqualTo(expected);
		}

		private InlineKeyboardMarkup buildButtons() {
				var inlineKeyboardMarkup = new InlineKeyboardMarkup();
				var moods = InitialDbValues.MOODS.stream().map(MoodContent::getMood).map(Mood::getText).toList();
				List<List<InlineKeyboardButton>> result = new ArrayList<>();
				for (int i = 0; i < moods.size(); i++) {
						var inline = new InlineKeyboardButton();
						inline.setText(moods.get(i));
						inline.setCallbackData(String.valueOf(i + 1));
						result.add(List.of(inline));
				}
				inlineKeyboardMarkup.setKeyboard(result);
				return inlineKeyboardMarkup;
		}

		@TestConfiguration
		public static class BotCommandHandlerTestTestConfig {

				@Bean
				public MoodRepository moodRepository() {
						return new MoodFakeRepository();
				}

				@Bean
				public MoodLogRepository moodLogRepository() {
						return new MoodLogFakeRepository();
				}

				@Bean
				public UserRepository userRepository() {
						return new UserFakeRepository();
				}

				@Bean
				public AchievementRepository achievementRepository() {
						return new AchievementsFakeRepository();
				}
		}

}