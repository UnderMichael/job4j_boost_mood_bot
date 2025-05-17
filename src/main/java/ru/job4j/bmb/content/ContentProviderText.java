package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.util.NoSuchElementException;

@Component
public class ContentProviderText implements ContentProvider {
		private final MoodContentRepository moodContentRepository;

		public ContentProviderText(MoodContentRepository moodContentRepository) {
				this.moodContentRepository = moodContentRepository;
		}

		@Override
		public Content byMood(Long chatId, Long moodId) {
				return this.moodContentRepository.findByMoodId(moodId).stream()
						.findFirst()
						.map(moodContent -> new Content(chatId)
								.setText(moodContent.getText())
						)
						.orElseThrow(() -> new NoSuchElementException("not image for mood"));
		}
}
