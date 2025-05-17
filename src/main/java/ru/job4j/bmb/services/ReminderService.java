package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.constants.Settings;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.model.UserSetting;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserSettingRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {
		private final SentContent sentContent;
		private final MoodLogRepository moodLogRepository;
		private final UserSettingRepository userSettingRepository;
		private final TgUI tgUI;
		private final MoodService moodService;

		public ReminderService(SentContent sentContent,
		                       MoodLogRepository moodLogRepository,
		                       UserSettingRepository userSettingRepository,
		                       TgUI tgUI,
		                       MoodService moodService) {
				this.sentContent = sentContent;
				this.moodLogRepository = moodLogRepository;
				this.userSettingRepository = userSettingRepository;
				this.tgUI = tgUI;
				this.moodService = moodService;
		}

		@Scheduled(fixedRateString = "${remind.period}")
		public void remindUsers() {
				var startOfDay = LocalDate.now()
						.atStartOfDay(ZoneId.systemDefault())
						.toInstant()
						.toEpochMilli();

				moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay)
						.stream()
						.map(user -> new Content(user.getChatId())
								.setMarkup(tgUI.buildButtons())
								.setText("Как настроение?"))
						.forEach(sentContent::sent);
		}

		@Scheduled(fixedRateString = "${recommendation.alert.period}")
		public void recommendationAlert() {

				userSettingRepository.findAll().stream()
						.filter(userSetting -> userSetting.getSetting().getCommand().equals(Settings.DAILY_ADVICE.getName()))
						.filter(UserSetting::isActive)
						.map(UserSetting::getUser)
						.map(user -> moodService.dailyAdvice(user.getChatId()))
						.forEach(content -> content.ifPresent(sentContent::sent));
		}

}
