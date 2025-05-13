package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_achievement")
public class Achievement {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private long createdAt;
		@ManyToOne
		@JoinColumn(name = "user_id")
		private User user;

		@ManyToOne
		@JoinColumn(name = "award_id")
		private Award award;

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}

				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				Achievement that = (Achievement) o;
				return Objects.equals(id, that.id);
		}

		@Override
		public int hashCode() {
				return Objects.hashCode(id);
		}

		public Long getId() {
				return id;
		}

		public Achievement setId(Long id) {
				this.id = id;
				return this;
		}

		public long getCreatedAt() {
				return createdAt;
		}

		public void setCreatedAt(long createdAt) {
				this.createdAt = createdAt;
		}

		public User getUser() {
				return user;
		}

		public Achievement setUser(User user) {
				this.user = user;
				return this;
		}

		public Award getAward() {
				return award;
		}

		public Achievement setAward(Award award) {
				this.award = award;
				return this;
		}
}
