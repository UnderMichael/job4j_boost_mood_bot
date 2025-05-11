package ru.job4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.job4j.bmb.constants.InitialDbValues;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;

@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class Main {

		public static void main(String[] args) {
				SpringApplication.run(Main.class, args);
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