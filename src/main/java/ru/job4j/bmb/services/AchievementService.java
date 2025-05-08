package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
		@PostConstruct
		public void init() {
				System.out.println(getClass().getSimpleName() + " is init");
		}

		@PreDestroy
		public void destroy() {
				System.out.println(getClass().getSimpleName() + " is destroying");
		}
}
