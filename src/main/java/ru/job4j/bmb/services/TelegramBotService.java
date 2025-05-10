package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;

@Service
public class TelegramBotService implements BeanNameAware {
		private final BotCommandHandler handler;

		public TelegramBotService(BotCommandHandler handler) {
				this.handler = handler;
		}

		@Override
		public void setBeanName(String name) {
				System.out.println(name);
		}

		public void receive(Content content) {
				handler.receive(content);
		}
}
