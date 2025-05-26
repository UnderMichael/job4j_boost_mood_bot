package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_content")
public class MoodContent {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@ManyToOne
		@JoinColumn(name = "mood_id")
		private Mood mood;
		private String text;
		private String image;
		private String audio;

		public MoodContent(Mood mood, String text, String image, String audio) {
				this.mood = mood;
				this.text = text;
				this.image = image;
				this.audio = audio;
		}

		public MoodContent() {

		}

		public String getAudio() {
				return audio;
		}

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}
				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				MoodContent that = (MoodContent) o;
				return Objects.equals(id, that.id);
		}

		@Override
		public int hashCode() {
				return Objects.hashCode(id);
		}

		public Long getId() {
				return id;
		}

		public MoodContent setId(Long id) {
				this.id = id;
				return this;
		}

		public Mood getMood() {
				return mood;
		}

		public void setMood(Mood mood) {
				this.mood = mood;
		}

		public String getText() {
				return text;
		}

		public void setText(String text) {
				this.text = text;
		}

		public String getImage() {
				return image;
		}
}
