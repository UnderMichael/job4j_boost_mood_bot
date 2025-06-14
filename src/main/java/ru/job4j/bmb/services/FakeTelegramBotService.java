package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.conditions.OnFakeTelegramMode;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.exceptions.SentContentException;

@Service
@Conditional(OnFakeTelegramMode.class)
public class FakeTelegramBotService extends TelegramLongPollingBot implements SentContent {
		final BotCommandHandler handler;
		final String botName;

		public FakeTelegramBotService(@Value("${telegram.bot.name}") String botName,
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
								System.out.println(SendAudio.builder()
										.audio(content.getAudio())
										.chatId(content.getChatId())
										.caption(content.getText())
										.build());
						} else if (content.getPhoto() != null) {
								System.out.println(SendPhoto.builder()
										.photo(content.getPhoto())
										.chatId(content.getChatId())
										.caption(content.getText())
										.build());
						} else if (content.getMarkup() != null) {
								System.out.println(SendMessage.builder()
										.replyMarkup(content.getMarkup())
										.text(content.getText())
										.chatId(content.getChatId())
										.build());
						} else {
								System.out.println(SendMessage.builder()
										.text(content.getText())
										.chatId(content.getChatId())
										.build());
						}
				} catch (Exception e) {
						throw new SentContentException(e.getMessage(), e);
				}
		}
}
