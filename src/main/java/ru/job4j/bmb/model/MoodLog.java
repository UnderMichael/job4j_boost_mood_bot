package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@ManyToOne
		@JoinColumn(name = "user_id")
		private User user;

		@ManyToOne
		@JoinColumn(name = "mood_id")
		private Mood mood;
		private long createdAt;

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}
				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				MoodLog moodLog = (MoodLog) o;
				return Objects.equals(id, moodLog.id);
		}

		@Override
		public int hashCode() {
				return Objects.hashCode(id);
		}

		public Long getId() {
				return id;
		}

		public MoodLog setId(Long id) {
				this.id = id;
				return this;
		}

		public User getUser() {
				return user;
		}

		public MoodLog setUser(User user) {
				this.user = user;
				return this;
		}

		public Mood getMood() {
				return mood;
		}

		public MoodLog setMood(Mood mood) {
				this.mood = mood;
				return this;
		}

		public long getCreatedAt() {
				return createdAt;
		}

		public MoodLog setCreatedAt(long createdAt) {
				this.createdAt = createdAt;
				return this;
		}

		public MoodLog setCreatedAt() {
				this.createdAt = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli();
				return this;
		}
}
