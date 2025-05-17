package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.constants.Settings;
import ru.job4j.bmb.model.Setting;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.model.UserSetting;
import ru.job4j.bmb.repository.UserSettingRepository;

@Service
public class SettingService {
		private final UserSettingRepository userSettingRepository;

		public SettingService(UserSettingRepository userSettingRepository) {
				this.userSettingRepository = userSettingRepository;
		}

		public void setUserSetting(User user, Setting setting) {
				var value = setting.getCommand().equals(Settings.YES.getName());
				userSettingRepository.findByUser(user).stream()
						.filter(userSetting -> userSetting.getSetting().equals(setting.getParent()))
						.findFirst()
						.ifPresentOrElse(
								userSetting -> userSetting.setActive(value),
								() -> userSettingRepository.save(new UserSetting(user, setting, value))
						);
		}
}
