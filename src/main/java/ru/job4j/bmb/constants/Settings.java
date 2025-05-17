package ru.job4j.bmb.constants;

public enum Settings {
		DAILY_ADVICE("Совет дня"),
		SETTINGS("Настройки"),
		YES("Вкл"),
		NO("Выкл");
		private final String value;

		Settings(String value) {
				this.value = value;
		}

		public String getValue() {
				return value;
		}

		public String getName() {
				return this.name();
		}

		@Override
		public String toString() {
				return value;
		}
}
