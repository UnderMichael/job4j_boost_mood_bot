package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_user_setting")
public class UserSetting {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@ManyToOne
		@JoinColumn(name = "user_id")
		private User user;
		@ManyToOne
		@JoinColumn(name = "setting_id")
		private Setting setting;
		private boolean isActive = false;

		public UserSetting(User user, Setting setting, boolean isActive) {
				this(user, setting);
				this.isActive = isActive;
		}

		public UserSetting(User user, Setting setting) {
				this.user = user;
				this.setting = setting.getChildren().isEmpty() ? setting.getParent() : setting;
		}

		public UserSetting() {

		}

		public User getUser() {
				return user;
		}

		public Setting getSetting() {
				return setting;
		}

		public boolean isActive() {
				return isActive;
		}

		public void setActive(boolean active) {
				isActive = active;
		}

		@Override
		public String toString() {
				return "UserSetting{" +
						"id=" + id +
						", user=" + user +
						", setting=" + setting +
						", isActive=" + isActive +
						'}';
		}
}
