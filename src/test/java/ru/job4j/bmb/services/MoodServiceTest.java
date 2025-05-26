package ru.job4j.bmb.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.constants.InitialDbValues;
import ru.job4j.bmb.content.ContentProviderAudio;
import ru.job4j.bmb.content.ContentProviderImage;
import ru.job4j.bmb.content.ContentProviderText;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.MoodContentFakeRepository;
import ru.job4j.bmb.repositories.MoodFakeRepository;
import ru.job4j.bmb.repositories.MoodLogFakeRepository;
import ru.job4j.bmb.repositories.UserFakeRepository;
import ru.job4j.bmb.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ContextConfiguration(classes = {
		UserFakeRepository.class,
		MoodLogFakeRepository.class,
		MoodFakeRepository.class,
		MoodContentFakeRepository.class,
		RecommendationEngine.class,
		ContentProviderImage.class,
		ContentProviderAudio.class,
		ContentProviderText.class,
		MoodService.class,
})
class MoodServiceTest {
		@Autowired
		private MoodRepository moodRepository;
		@Autowired
		private MoodLogRepository moodLogRepository;
		@Autowired
		private MoodContentRepository moodContentRepository;
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private MoodService moodService;
		@Autowired
		private RecommendationEngine recommendationEngine;
		@MockBean
		private AchievementRepository achievementRepository;

		@BeforeEach
		void setup() {
				userRepository.deleteAll();
				var moods = moodRepository.findAll();
				if (!moods.isEmpty()) {
						return;
				}
				var moodsContent = InitialDbValues.MOODS;
				List<Mood> moodsResult = new ArrayList<>();
				List<MoodContent> moodsContentResult = new ArrayList<>();
				for (int i = 0; i < moodsContent.size(); i++) {
						moodsResult.add(moodsContent.get(i).getMood().setId((long) i + 1));
						moodsContentResult.add(moodsContent.get(i).setId((long) i + 1));
				}
				moodRepository.saveAll(moodsResult);
				moodContentRepository.saveAll(moodsContentResult);
		}

		@Test
		void dailyAdviceWhereNoSuchUser() {
				assertThatThrownBy(() -> {
						moodService.dailyAdvice(0);
				})
						.isInstanceOf(NoSuchElementException.class)
						.hasMessageContaining("User not found");
		}

		@Test
		void dailyAdviceWhenNoMoodLog() {
				userRepository.save(
						new User()
								.setChatId(1)
				);
				assertThat(moodService.dailyAdvice(1)).isNotPresent();
		}

		@Test
		void dailyAdviceFirstMoodLog() {
				var user = userRepository.save(
						new User()
								.setChatId(1)
								.setId(1L)
				);
				var moodContent = moodContentRepository.findByMoodId(3L).get();
				moodLogRepository.save(
						new MoodLog()
								.setMood(moodContent.getMood())
								.setUser(user)
				);
				var expected = List.of(
						new ContentProviderAudio(moodContentRepository).byMood(1L, 3L),
						new ContentProviderImage(moodContentRepository).byMood(1L, 3L),
						new ContentProviderText(moodContentRepository).byMood(1L, 3L)
				);
				assertThat(moodService.dailyAdvice(1).get()).isIn(expected);
		}

		@Test
		void dailyAdviceLastByDateCreated() {
				var user = userRepository.save(
						new User()
								.setChatId(1)
								.setId(1L)
				);
				var moodContentFirst = moodContentRepository.findByMoodId(3L).get();
				var moodContentSecond = moodContentRepository.findByMoodId(2L).get();
				moodLogRepository.save(
						new MoodLog()
								.setMood(moodContentFirst.getMood())
								.setId(1L)
								.setCreatedAt(500)
								.setUser(user)
				);
				moodLogRepository.save(
						new MoodLog()
								.setMood(moodContentSecond.getMood())
								.setId(2L)
								.setCreatedAt(100)
								.setUser(user)
				);
				var expected = List.of(
						new ContentProviderAudio(moodContentRepository).byMood(1L, 3L),
						new ContentProviderImage(moodContentRepository).byMood(1L, 3L),
						new ContentProviderText(moodContentRepository).byMood(1L, 3L)
				);
				assertThat(moodService.dailyAdvice(1).get()).isIn(expected);
		}

		@TestConfiguration
		public static class MoodServiceTestConfig {

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
				public MoodContentRepository moodContentRepository() {
						return new MoodContentFakeRepository();
				}
		}
}