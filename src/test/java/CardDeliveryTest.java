import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setUp() {
        // По замечанию преподавателя настройки размера окна и дефолтного таймаута убраны.
        // Headless-режим передается через параметр в CI (gradle.yml).
        // Configuration.browser = "firefox"; // Если нужно локально с окном - раскомментируй
    }

    @Test
    void shouldOrderCardSuccessfully() {
        // 1. Открываем страницу
        open("http://localhost:9999");

        // 2. Генерируем дату (текущая + 3 дня)
        LocalDate date = LocalDate.now().plusDays(3);
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 3. Заполняем форму с помощью тестовых меток (data-test-id)
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(dateString);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001234567");
        $("[data-test-id='agreement']").click();

        // Кнопка "Забронировать"
        $("button.button").click();

        // 4. Проверяем результат
        // Ждем появления заголовка уведомления (не дольше 15 секунд)
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно"));

        // Проверяем весь текст сообщения с датой
        String expectedMessage = "Встреча успешно забронирована на " + dateString;
        $(".notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedMessage));
    }
}