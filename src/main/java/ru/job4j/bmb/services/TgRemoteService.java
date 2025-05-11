package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

		private final String botName;
		private final String botToken;
		private final UserRepository userRepository;
		private final TgUI tgUI;
		private final BotCommandHandler handler;

		public TgRemoteService(@Value("${telegram.bot.name}") String botName,
		                       @Value("${telegram.bot.token}") String botToken,
		                       UserRepository userRepository,
		                       TgUI tgUI, BotCommandHandler handler
		) {
				this.botName = botName;
				this.botToken = botToken;
				this.userRepository = userRepository;
				this.tgUI = tgUI;
				this.handler = handler;
		}

		@Override
		public String getBotUsername() {
				return botName;
		}

		@Override
		public String getBotToken() {
				return botToken;
		}

		@Override
		public void onUpdateReceived(Update update) {
				if (update.hasCallbackQuery()) {
						handler.handleCallback(update.getCallbackQuery())
								.ifPresent(content -> send(new SendMessage(content.getChatId().toString(), content.getText())));
				}
				if (update.hasMessage() && update.getMessage().hasText()) {
						handler.commands(update.getMessage())
								.ifPresent(content -> send(new SendMessage(content.getChatId().toString(), content.getText())));

				}

		}

		public void send(SendMessage message) {
				try {
						execute(message);
				} catch (TelegramApiException e) {
						e.printStackTrace();
				}
		}
}
