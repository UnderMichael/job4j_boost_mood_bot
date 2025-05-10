package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;

@Service
public class BotCommandHandler implements BeanNameAware {
		@Override
		public void setBeanName(String name) {
				System.out.println(name);
		}

		void receive(Content content) {
				System.out.println(content);
		}

}
