package ru.netology;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class LogInAutoTest {
    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }
    @Test
    public void testUserActive() {
        DataClass user = DataGenerator.Registration.getRegisteredUser("active");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(user.getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(user.getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $x("//*/h2[contains(@class,\"heading\")]")
                .shouldHave(visible,Duration.ofSeconds(15))
                .shouldHave(text("Личный кабинет"));
    }

    @Test
    public void testUserBlocked() {
        DataClass user = DataGenerator.Registration.getRegisteredUser("blocked");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(user.getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(user.getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Пользователь заблокирован"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserDoesntExist() {
        DataClass user = DataGenerator.Registration.getUser("active");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(user.getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(user.getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }


    @Test
    public void testUserWrongPassword() {
        DataClass user = DataGenerator.Registration.getRegisteredUser("active");
        String wrongPassword = DataGenerator.getPassword();

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(user.getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(wrongPassword);
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserWrongLogin() {
        DataClass user = DataGenerator.Registration.getRegisteredUser("active");
        String wrongLogin = DataGenerator.getLogin();

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(wrongLogin);
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(user.getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }
}
