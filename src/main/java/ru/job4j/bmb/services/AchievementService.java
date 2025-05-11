package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.repositories.AchievementRepository;
import ru.job4j.bmb.repositories.AwardRepository;
import ru.job4j.bmb.repositories.MoodLogRepository;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
		private final MoodLogRepository moodLogRepository;
		private final AwardRepository awardRepository;
		private final AchievementRepository achievementRepository;
		private final TelegramBotService telegramBotService;

		public AchievementService(MoodLogRepository moodLogRepository, AwardRepository awardRepository, AchievementRepository achievementRepository, TelegramBotService telegramBotService) {
				this.moodLogRepository = moodLogRepository;
				this.awardRepository = awardRepository;
				this.achievementRepository = achievementRepository;
				this.telegramBotService = telegramBotService;
		}

		@Transactional
		@Override
		public void onApplicationEvent(UserEvent event) {
				var user = event.getUser();

				var goodMoodDays = moodLogRepository.findByUser(user).stream()
						.filter(moodLog -> moodLog.getMood().isGood())
						.count();
				var achievementsAchieved = achievementRepository.findByUser(user).stream()
						.map(Achievement::getAward)
						.toList();
				awardRepository.findAll().stream()
						.filter(award -> award.getDays() <= goodMoodDays)
						.filter(award -> !achievementsAchieved.contains(award))
						.forEach(award -> {
								telegramBotService.sent(
										new Content(user.getChatId())
												.setText(
														award.getTitle() + ": " + award.getDescription())
								);
								achievementRepository.save(
										new Achievement()
												.setAward(award)
												.setUser(user)
								);
						});
		}

}
