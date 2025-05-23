package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderServiceTest {
		@Test
		public void whenMoodGood() {
				var result = new ArrayList<Content>();
				var sentContent = new SentContent() {
						@Override
						public void sent(Content content) {
								result.add(content);
						}
				};
				var moodRepository = new MoodFakeRepository();
				var settingsRepository = new SettingsFakeRepository();
				var userSettingsRepository = new UserSettingFakeRepository();
				moodRepository.save(new Mood("Good", true));
				var moodLogRepository = new MoodLogFakeRepository();
				var user = new User();
				user.setChatId(100);
				var moodLog = new MoodLog();
				moodLog.setUser(user);
				var yesterday = LocalDate.now()
						.minusDays(10)
						.atStartOfDay(ZoneId.systemDefault())
						.toInstant()
						.toEpochMilli() - 1;
				moodLog.setCreatedAt(yesterday);
				moodLogRepository.save(moodLog);
				var tgUI = new TgUI(moodRepository, settingsRepository);
				var moodService = new MoodService(moodRepository, moodLogRepository, new RecommendationEngine(List.of()),
						new UserFakeRepository(), new AchievementsFakeRepository(), new ApplicationEventPublisher() {
						@Override
						public void publishEvent(Object event) {

						}
				});
				new ReminderService(sentContent, moodLogRepository, userSettingsRepository, tgUI, moodService)
						.remindUsers();
				assertThat(result.iterator().next().getMarkup().getKeyboard()
						.iterator().next().iterator().next().getText()).isEqualTo("Good");
		}
}
