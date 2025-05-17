package ru.job4j.bmb.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.List;

@Component
public class TgUI {
		private final MoodRepository moodRepository;

		public TgUI(MoodRepository moodRepository) {
				this.moodRepository = moodRepository;
		}

		public InlineKeyboardMarkup buildButtons() {
				var inlineKeyboardMarkup = new InlineKeyboardMarkup();
				inlineKeyboardMarkup.setKeyboard(
						moodRepository.findAll()
								.stream()
								.map(mood -> List.of(createBtn(mood.getText(), mood.getId())))
								.toList());
				return inlineKeyboardMarkup;
		}

		private InlineKeyboardButton createBtn(String name, Long moodId) {
				var inline = new InlineKeyboardButton();
				inline.setText(name);
				inline.setCallbackData(String.valueOf(moodId));
				return inline;
		}
}
