package ru.job4j.bmb.constants;

import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.model.Setting;

import java.util.List;

public class InitialDbValues {
		public static final List<MoodContent> MOODS = List.of(
				new MoodContent(
						new Mood("Счастливейший на свете \uD83D\uDE0E", true),
						"Невероятно! Вы сияете от счастья, продолжайте радоваться жизни.",
						"1.jpg",
						"The happiest man in the world.mp3"
				),
				new MoodContent(
						new Mood("Успокоение и гармония \ud83e\uddd8\u200d\u2642", true),
						"Потрясающе! Вы в состоянии внутреннего мира и гармонии.",
						"2.jpg",
						"Calm and Harmony.mp3"
				),
				new MoodContent(
						new Mood("В состоянии комфорта \u263a", true),
						"Отлично! Вы чувствуете себя уютно и спокойно.",
						"3.jpg",
						"In a state of comfort.mp3"
				),
				new MoodContent(
						new Mood("Легкое волнение \ud83c\udf88", true),
						"Замечательно! Немного волнения добавляет жизни краски.",
						"4.jpg",
						"A slight excitement.mp3"
				),
				new MoodContent(
						new Mood("Сосредоточенное настроение \ud83c\udfaf", true),
						"Хорошо! Ваш фокус на высоте, используйте это время эффективно.",
						"5.jpg",
						"Concentrated.mp3"
				),
				new MoodContent(
						new Mood("Тревожное настроение \ud83d\ude1f", false),
						"Не волнуйтесь, всё пройдет. Попробуйте расслабиться и найти источник вашего беспокойства.",
						"6.jpg",
						"Anxious.mp3"),
				new MoodContent(
						new Mood("Разочарованное настроение \ud83d\ude1e", false),
						"Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится.",
						"7.jpg",
						"Disappointed.mp3"
				),
				new MoodContent(
						new Mood("Усталое настроение \ud83d\ude34", false),
						"Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните.",
						"8.jpg",
						"Tired.mp3"
				),
				new MoodContent(
						new Mood("Вдохновенное настроение \ud83d\udca1", true),
						"Потрясающе! Вы полны идей и энергии для их реализации.",
						"9.jpg",
						"Inspired.mp3"
				),
				new MoodContent(
						new Mood("Раздраженное настроение \ud83d\ude20", false),
						"Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию.",
						"10.jpg",
						"Irritated.mp3"
				));
		public static final List<Setting> SETTINGS = List.of(
				new Setting(Settings.SETTINGS, "Настройки")
						.addChild(
								new Setting(Settings.DAILY_ADVICE, "Включить автоматическую отправку?")
										.addChild(new Setting(Settings.YES))
										.addChild(new Setting(Settings.NO))
						)
		);
		public static final List<Award> AWARDS = List.of(new Award("Смайлик дня", "За 1 день хорошего настроения.", 1),
				new Award("Настроение недели",
						"За 7 последовательных дней хорошего или отличного настроения. "
								+ "Награда: Специальный значок или иконка, "
								+ "отображаемая в профиле пользователя в течение недели.", 7),
				new Award("Бонусные очки",
						"За каждые 3 дня хорошего настроения. Награда: "
								+ "Очки, которые можно обменять на виртуальные предметы или функции внутри приложения.",
						3),
				new Award("Персонализированные рекомендации",
						"После 5 дней хорошего настроения. "
								+ "Награда: Подборка контента или активности на основе интересов пользователя.", 5),
				new Award("Достижение 'Солнечный луч'",
						"За 10 дней непрерывного хорошего настроения. "
								+ "Награда: Разблокировка новой темы оформления или фона в приложении.", 10),
				new Award("Виртуальный подарок",
						"После 15 дней хорошего настроения. "
								+ "Награда: Возможность отправить или"
								+ " получить виртуальный подарок внутри приложения.", 15),
				new Award("Титул 'Лучезарный'",
						"За 20 дней хорошего или отличного настроения. "
								+ "Награда: Специальный титул, отображаемый рядом с именем пользователя.", 20),
				new Award("Доступ к премиум-функциям",
						"После 30 дней хорошего настроения. "
								+ "Награда: Временный доступ к премиум-функциям или эксклюзивному контенту.", 30),
				new Award("Участие в розыгрыше призов",
						"За каждую неделю хорошего настроения. "
								+ "Награда: Шанс выиграть призы в ежемесячных розыгрышах.", 7),
				new Award("Эксклюзивный контент",
						"После 25 дней хорошего настроения. "
								+ "Награда: Доступ к эксклюзивным статьям, видео или мероприятиям.", 25),
				new Award("Награда 'Настроение месяца'",
						"За поддержание хорошего или отличного настроения в течение целого месяца."
								+ "Награда: Специальный значок, признание в сообществе или дополнительные привилегии.", 30),
				new Award("Физический подарок",
						"После 60 дней хорошего настроения. "
								+ "Награда: Возможность получить "
								+ "небольшой физический подарок, например, открытку или фирменный сувенир.", 60),
				new Award("Коучинговая сессия",
						"После 45 дней хорошего настроения. Награда: Бесплатная сессия с коучем или консультантом для "
								+ "дальнейшего улучшения благополучия.", 45),
				new Award("Разблокировка мини-игр",
						"После 14 дней хорошего настроения. Награда: Доступ к развлекательным мини-играм внутри приложения.",
						14),
				new Award("Персональное поздравление",
						"За значимые достижения (например, 50 дней хорошего настроения). Награда: Персонализированное "
								+ "сообщение от команды приложения или вдохновляющая цитата.",
						50));
}
