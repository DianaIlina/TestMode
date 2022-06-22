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


public class LogInAutoTest {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static DataClass[] users;

    @BeforeAll
    static void setUpAll() {
        users = DataGenerator.LogIn.generateUsers();
        for (DataClass user : users) {
            given()
                    .spec(requestSpec)
                    .body(new DataClass(user.getLogin(), user.getPassword(), user.getStatus()))
                    .when()
                    .post("/api/system/users")
                    .then() // "тогда ожидаем"
                    .statusCode(200);
        }
    }

    @Test
    public void testUserActive() {

        Configuration.headless = true;
        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(users[0].getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(users[0].getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $x("//*/div[@class=\"App_appContainer__3jRx1\"]")
                .shouldHave(visible,Duration.ofSeconds(15));
    }

    @Test
    public void testUserBlocked() {
        Configuration.headless = true;

        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(users[1].getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(users[1].getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Пользователь заблокирован"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserWrongLogin() {
        Configuration.headless = true;

        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(users[0].getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(users[1].getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }

    @Test
    public void testUserWrongPassword() {
        Configuration.headless = true;

        open("http://localhost:9999");

        $x("//*/span[@data-test-id=\"login\"]//input").setValue(users[1].getLogin());
        $x("//*/span[@data-test-id=\"password\"]//input").setValue(users[0].getPassword());
        $x("//*/button[@data-test-id=\"action-login\"]").click();

        $(byText("Неверно указан логин или пароль"))
                .shouldHave(visible, Duration.ofSeconds(10));
    }
}
