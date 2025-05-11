package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.exceptions.SentContentException;
import ru.job4j.bmb.model.Content;

@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
		private final BotCommandHandler handler;
		private final String botName;

		public TelegramBotService(@Value("${telegram.bot.name}") String botName,
		                          @Value("${telegram.bot.token}") String botToken,
		                          BotCommandHandler handler) {
				super(botToken);
				this.handler = handler;
				this.botName = botName;
		}

		@Override
		public void onUpdateReceived(Update update) {
				if (update.hasCallbackQuery()) {
						handler.handleCallback(update.getCallbackQuery())
								.ifPresent(this::sent);
				} else if (update.hasMessage() && update.getMessage().getText() != null) {
						handler.commands(update.getMessage())
								.ifPresent(this::sent);
				}
		}

		@Override
		public String getBotUsername() {
				return botName;
		}

		@Override
		public void sent(Content content) {
				try {
						if (content.getAudio() != null) {
								execute(SendAudio.builder()
										.audio(content.getAudio())
										.chatId(content.getChatId())
										.caption(content.getText())
										.build());
						} else if (content.getPhoto() != null) {
								execute(SendPhoto.builder()
										.photo(content.getPhoto())
										.chatId(content.getChatId())
										.caption(content.getText())
										.build());
						} else {
								execute(SendMessage.builder()
										.replyMarkup(content.getMarkup())
										.text(content.getText())
										.chatId(content.getChatId())
										.build());
						}
				} catch (TelegramApiException e) {
						throw new SentContentException(e.getMessage(), e);
				}
		}
}
