package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;

@Service
public class AchievementService implements BeanNameAware {
		@PostConstruct
		public void init() {
				System.out.println(getClass().getSimpleName() + " is init");
		}

		@PreDestroy
		public void destroy() {
				System.out.println(getClass().getSimpleName() + " is destroying");
		}

		@Override
		public void setBeanName(String name) {
				System.out.println(name);
		}
}
