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
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
        Configuration.headless = true;
        Configuration.timeout = 15000;
    }

    @Test
    void shouldOrderCardSuccessfully() {
        open("http://localhost:9999");

        LocalDate date = LocalDate.now().plusDays(3);
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Заполняем поля по индексам
        $$("input").get(0).setValue("Казань");
        $$("input").get(1).setValue(dateString);
        $$("input").get(2).setValue("Иванов Иван");
        $$("input").get(3).setValue("+79001234567");

        // Клик по видимой обертке чекбокса
        $(".checkbox").click();

        // Кнопка
        $("button.button").click();

        // Проверка
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно"));
    }
}