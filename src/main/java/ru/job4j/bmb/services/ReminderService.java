package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.repositories.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {
		private final SentContent sentContent;
		private final MoodLogRepository moodLogRepository;
		private final TgUI tgUI;

		public ReminderService(SentContent sentContent, MoodLogRepository moodLogRepository, TgUI tgUI) {
				this.sentContent = sentContent;
				this.moodLogRepository = moodLogRepository;
				this.tgUI = tgUI;
		}

		@Scheduled(fixedRateString = "${recommendation.alert.period}")
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

}
