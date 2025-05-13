package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.constants.InitialDbValues;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.MoodFakeRepository;
import ru.job4j.bmb.repositories.MoodLogFakeRepository;
import ru.job4j.bmb.repositories.UserFakeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {UserFakeRepository.class, MoodLogFakeRepository.class, MoodFakeRepository.class})
class BotCommandHandlerTest {

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
		public void whenWeekMoodLogCommand() {
				var user = new User()
						.setChatId(1)
						.setClientId(1);
				userRepository.save(user);

				List<Mood> moods = InitialDbValues.MOODS.stream().map(MoodContent::getMood).toList();
				moodRepository.saveAll(moods);
				moodLogRepository.saveAll(List.of(
						new MoodLog().setUser(user).setMood(moods.get(0)),
						new MoodLog().setUser(user).setMood(moods.get(1)),
						new MoodLog().setUser(user).setMood(moods.get(2))
				));
				moodLogRepository.findAll().stream().forEach(log -> System.out.println(log.getMood()));
				var input = new Message();
				input.setText("/week_mood_log");
				input.setFrom(new org.telegram.telegrambots.meta.api.objects.User(1L, "", false));
				input.setChat(new Chat(1L, ""));
				var result = botCommandHandler.commands(input);
				System.out.println(result.get().getText());
				var expected = Optional.of(
						new Content(1L)
								.setText("Как настроение?")
								.setMarkup(buildButtons()));
				assertThat(result).isPresent().isEqualTo(expected);
		}

		private InlineKeyboardMarkup buildButtons() {
				var inlineKeyboardMarkup = new InlineKeyboardMarkup();
				var moods = InitialDbValues.MOODS;
				List<List<InlineKeyboardButton>> result = new ArrayList<>();
				for (int i = 0; i < moods.size(); i++) {
						var inline = new InlineKeyboardButton();
						inline.setText(moods.get(i).getMood().getText());
						inline.setCallbackData(String.valueOf(i + 1));
						result.add(List.of(inline));
				}
				inlineKeyboardMarkup.setKeyboard(result);
				return inlineKeyboardMarkup;
		}

}