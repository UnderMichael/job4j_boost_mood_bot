package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "mb_user")
public class User {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column(name = "client_id")
		private long clientId;

		@Column(name = "chat_id")
		private long chatId;
		@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<UserSetting> settings = new ArrayList<>();

		public List<UserSetting> getSettings() {
				return settings;
		}

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}
				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				User user = (User) o;
				return Objects.equals(id, user.id);
		}

		@Override
		public int hashCode() {
				return Objects.hash(id);
		}

		public Long getId() {
				return id;
		}

		public void setId(Long id) {
				this.id = id;
		}

		public long getClientId() {
				return clientId;
		}

		public User setClientId(long clientId) {
				this.clientId = clientId;
				return this;
		}

		public long getChatId() {
				return chatId;
		}

		public User setChatId(long chatId) {
				this.chatId = chatId;
				return this;
		}
}
