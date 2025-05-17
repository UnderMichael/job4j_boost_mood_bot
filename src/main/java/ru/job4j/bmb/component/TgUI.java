package ru.job4j.bmb.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.model.Setting;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.SettingRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class TgUI {
		private final MoodRepository moodRepository;
		private final SettingRepository settingRepository;

		public TgUI(MoodRepository moodRepository,
		            SettingRepository settingRepository) {
				this.moodRepository = moodRepository;
				this.settingRepository = settingRepository;
		}

		public InlineKeyboardMarkup buildButtons() {
				var inlineKeyboardMarkup = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rows = new ArrayList<>();
				moodRepository.findAll()
						.stream()
						.map(mood -> List.of(createBtn(mood.getText(), mood.getId())))
						.forEach(rows::add);
				settingRepository.findAll().stream()
						.filter(setting -> setting.getParent() == null)
						.map(setting -> List.of(createBtn(setting.getText(), setting.getCommand())))
						.forEach(rows::add);
				inlineKeyboardMarkup.setKeyboard(rows);
				return inlineKeyboardMarkup;
		}

		public InlineKeyboardMarkup buildButtons(List<Setting> settings) {
				var inlineKeyboardMarkup = new InlineKeyboardMarkup();
				inlineKeyboardMarkup.setKeyboard(settings.stream()
						.map(setting -> List.of(createBtn(setting.getText(), setting.getCommand())))
						.toList());
				return inlineKeyboardMarkup;
		}

		private InlineKeyboardButton createBtn(String name, Long moodId) {
				return createBtn(name, String.valueOf(moodId));
		}

		private InlineKeyboardButton createBtn(String name, String command) {
				var inline = new InlineKeyboardButton();
				inline.setText(name);
				inline.setCallbackData(String.valueOf(command));
				return inline;
		}
}
