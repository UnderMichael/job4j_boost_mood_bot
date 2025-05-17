package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.io.File;
import java.util.NoSuchElementException;

@Component
public class ContentProviderImage implements ContentProvider {
		private final MoodContentRepository moodContentRepository;

		public ContentProviderImage(MoodContentRepository moodContentRepository) {
				this.moodContentRepository = moodContentRepository;
		}

		@Override
		public Content byMood(Long chatId, Long moodId) {
				return this.moodContentRepository.findByMoodId(moodId).stream()
						.findFirst()
						.map(moodContent -> new Content(chatId)
								.setPhoto(new InputFile(
										new File(
												"./images/" + moodContent.getImage())))
						)
						.orElseThrow(() -> new NoSuchElementException("not image for mood"));
		}
}
