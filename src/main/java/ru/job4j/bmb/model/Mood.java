package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood")
public class Mood {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		private String text;

		private boolean good;

		public Mood(String text, boolean good) {
				this.text = text;
				this.good = good;
		}

		public Mood() {

		}

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}
				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				Mood mood = (Mood) o;
				return Objects.equals(id, mood.id);
		}

		@Override
		public int hashCode() {
				return Objects.hashCode(id);
		}

		public Long getId() {
				return id;
		}

		public Mood setId(Long id) {
				this.id = id;
				return this;
		}

		public String getText() {
				return text;
		}

		public void setText(String text) {
				this.text = text;
		}

		public boolean isGood() {
				return good;
		}

		public void setGood(boolean good) {
				this.good = good;
		}
}
