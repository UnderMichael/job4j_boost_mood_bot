package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.bmb.constants.InitialDbValues;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.services.TelegramBot;

@EnableScheduling
@SpringBootApplication
public class MoodBotApplication {
		public static void main(String[] args) {
				SpringApplication.run(MoodBotApplication.class, args);
		}

		@Bean
		public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
				return args -> {
						var bot = ctx.getBean(TelegramBot.class);
						var botsApi = new TelegramBotsApi(DefaultBotSession.class);
						try {
								botsApi.registerBot(bot);
								System.out.println("success!");
						} catch (TelegramApiException e) {
								e.printStackTrace();
						}
				};
		}

		@Bean
		CommandLineRunner loadDatabase(MoodRepository moodRepository,
		                               MoodContentRepository moodContentRepository,
		                               AwardRepository awardRepository) {
				return args -> {
						var moods = moodRepository.findAll();
						if (!moods.isEmpty()) {
								return;
						}
						moodRepository.saveAll(InitialDbValues.MOODS.stream().map(MoodContent::getMood).toList());
						moodContentRepository.saveAll(InitialDbValues.MOODS);
						awardRepository.saveAll(InitialDbValues.AWARDS);
				};
		}
}
