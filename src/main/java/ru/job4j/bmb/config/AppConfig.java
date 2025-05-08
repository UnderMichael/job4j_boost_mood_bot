package ru.job4j.bmb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
		@Value("${telegram.bot.name}")
		String botName;

		public void printConfig() {
				System.out.println(botName);
		}
}
