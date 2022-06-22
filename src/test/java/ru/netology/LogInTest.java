package ru.netology;

import com.codeborne.selenide.Configuration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

public class LogInTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    //private static DataClass[] users = new DataClass[];
    @BeforeAll
    static void setUpAll() {
        //users = DataGenerator.getUsers();
        // сам запрос
        given() // "дано"
            .spec(requestSpec) // указываем, какую спецификацию используем
            .body(new DataClass("vasya", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
        .when() // "когда"
            .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
        .then() // "тогда ожидаем"
            .statusCode(200); // код 200 OK

        given()
                .spec(requestSpec)
                .body(new DataClass("petya", "notapassword", "blocked"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUserActive() {
        Configuration.headless = true;
        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue("vasya");
        $x("//*/span[@data-test-id=\"password\"]//input").setValue("password");
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $x("//*/div[@class=\"App_appContainer__3jRx1\"]")
                .shouldHave(visible,Duration.ofSeconds(15));
    }

    @Test
    public void testUserBlocked() {
        Configuration.headless = true;
        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue("petya");
        $x("//*/span[@data-test-id=\"password\"]//input").setValue("notapassword");
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Пользователь заблокирован"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserWrongLogin() {
        Configuration.headless = true;
        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue("petya");
        $x("//*/span[@data-test-id=\"password\"]//input").setValue("password");
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserWrongPassword() {
        Configuration.headless = true;
        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue("vasya");
        $x("//*/span[@data-test-id=\"password\"]//input").setValue("notapassword");
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }
}
