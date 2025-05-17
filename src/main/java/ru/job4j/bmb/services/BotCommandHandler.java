package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.component.TgUI;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Setting;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.SettingRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BotCommandHandler {
		private final UserRepository userRepository;
		private final MoodService moodService;
		private final TgUI tgUI;
		private final SettingRepository settingRepository;
		private final SettingService settingService;

		public BotCommandHandler(UserRepository userRepository,
		                         MoodService moodService,
		                         TgUI tgUI,
		                         SettingRepository settingRepository, SettingService settingService) {
				this.userRepository = userRepository;
				this.moodService = moodService;
				this.tgUI = tgUI;
				this.settingRepository = settingRepository;
				this.settingService = settingService;
		}

		Optional<Content> commands(Message message) {
				return switch (message.getText()) {
						case "/start" -> handleStartCommand(message.getChatId(), message.getFrom().getId());
						case "/week_mood_log" -> moodService.weekMoodLogCommand(message.getChatId());
						case "/month_mood_log" -> moodService.monthMoodLogCommand(message.getChatId());
						case "/award" -> moodService.awards(message.getChatId());
						case "/daily_advice" -> moodService.dailyAdvice(message.getChatId());
						default -> Optional.empty();
				};
		}

		@Transactional
		protected Optional<Content> handleSettings(User user, Setting setting) {
				if (setting.getChildren().isEmpty()) {
						settingService.setUserSetting(user, setting);
						return handleStartCommand(user.getChatId(), user.getClientId());
				} else {
						return Optional.of(
								new Content(user.getChatId())
										.setMarkup(tgUI.buildButtons(setting.getChildren()))
										.setText(setting.getDesc())
						);
				}
		}

		@Transactional
		Optional<Content> handleCallback(CallbackQuery callback) {
				var user = userRepository.findByChatId(callback.getFrom().getId()).stream()
						.findFirst()
						.orElseThrow(() -> new NoSuchElementException("user not found"));
				var setting = settingRepository.findByCommand(callback.getData()).stream().findFirst();
				if (setting.isPresent()) {
						return handleSettings(user, setting.get());
				}
				var moodId = Long.valueOf(callback.getData());
				return Optional.of(moodService.chooseMood(user, moodId));
		}

		private Optional<Content> handleStartCommand(long chatId, Long clientId) {
				var user = userRepository.findByChatId(chatId)
						.orElseGet(() -> new User()
								.setClientId(clientId)
								.setChatId(chatId));
				if (user.getId() == null) {
						userRepository.save(user);
				}
				return Optional.of(
						new Content(user.getChatId())
								.setText("Как настроение?")
								.setMarkup(tgUI.buildButtons())
				);
		}
}
