package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.job4j.bmb.content.SentContent;

public abstract class TelegramBot extends TelegramLongPollingBot implements SentContent {
		final BotCommandHandler handler;
		final String botName;

		public TelegramBot(@Value("${telegram.bot.name}") String botName,
		                   @Value("${telegram.bot.token}") String botToken,
		                   BotCommandHandler handler) {
				super(botToken);
				this.handler = handler;
				this.botName = botName;
		}
}
