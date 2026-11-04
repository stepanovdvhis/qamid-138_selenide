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
        // Преподаватель просил убрать настройки размера окна и дефолтного таймаута.
        // Headless-режим мы передаем через параметр командной строки в CI (gradle.yml).
        // Если локально нужно запустить с окном, можно временно раскомментировать строку ниже.
        // Configuration.browser = "firefox";
    }

    @Test
    void shouldOrderCardSuccessfully() {
        // 1. Открываем страницу
        open("http://localhost:9999");

        // 2. Генерируем дату (текущая + 3 дня)
        LocalDate date = LocalDate.now().plusDays(3);
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 3. Заполняем форму с помощью тестовых меток (data-test-id)
        // Если локально эти селекторы не находятся, значит, в вашей версии SUT их нет,
        // но в CI (на GitHub) они точно есть, судя по замечанию преподавателя.

        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(dateString);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001234567");

        // Клик по чекбоксу согласия
        $("[data-test-id='agreement']").click();

        // Кнопка "Забронировать"
        $("button.button").click();

        // 4. Проверяем результат
        // Ждем появления уведомления об успехе не дольше 15 секунд
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно"));

        // Проверяем, что в сообщении об успехе фигурирует именно наша дата
        $(".notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(dateString));
    }
}