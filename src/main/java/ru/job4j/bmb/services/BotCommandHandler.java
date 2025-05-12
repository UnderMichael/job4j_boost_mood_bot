package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
		private final UserRepository userRepository;
		private final MoodService moodService;
		private final TgUI tgUI;

		public BotCommandHandler(UserRepository userRepository, MoodService moodService, TgUI tgUI) {
				this.userRepository = userRepository;
				this.moodService = moodService;
				this.tgUI = tgUI;
		}

		Optional<Content> commands(Message message) {
				return switch (message.getText()) {
						case "/start" -> handleStartCommand(message.getChatId(), message.getFrom().getId());
						case "/week_mood_log" -> moodService.weekMoodLogCommand(message.getChatId(), message.getFrom().getId());
						case "/month_mood_log" -> moodService.monthMoodLogCommand(message.getChatId(), message.getFrom().getId());
						case "/award" -> moodService.awards(message.getChatId(), message.getFrom().getId());
						default -> Optional.empty();
				};
		}

		Optional<Content> handleCallback(CallbackQuery callback) {
				var moodId = Long.valueOf(callback.getData());
				var user = userRepository.findByChatId(callback.getFrom().getId());
				return Optional.of(moodService.chooseMood(user.get(), moodId));
		}

		private Optional<Content> handleStartCommand(long chatId, Long clientId) {
				var user = userRepository.findByChatId(chatId)
						.orElseGet(() -> new User()
								.setClientId(clientId)
								.setChatId(chatId));
				if (user.getId() == null) {
						System.out.println("created");
						userRepository.save(user);
				}
				return Optional.of(
						new Content(user.getChatId())
								.setText("Как настроение?")
								.setMarkup(tgUI.buildButtons())
				);
		}
}
