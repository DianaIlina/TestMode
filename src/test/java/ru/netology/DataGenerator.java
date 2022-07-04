package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
        .setBaseUri("http://localhost")
        .setPort(9999)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    private static final Faker faker = new Faker (new Locale("en"));

    public static String getLogin() {
        return faker.name().username();
    }
    public static String getPassword() {
        return faker.internet().password();
    }

    public static void sendRequest(DataClass body) {
        given()
            .spec(requestSpec)
            .body(body)
            .when()
            .post("/api/system/users")
            .then() // "тогда ожидаем"
            .statusCode(200);
    }


    @UtilityClass
    public static class Registration {
        public static DataClass getRegisteredUser(String status) {
            DataClass user = getUser(status);
            sendRequest(user);
            return user;
        }
        public static DataClass getUser(String status) {
            String login = getLogin();
            String password = getPassword();
            DataClass user = new DataClass(login, password, status);
            return user;
        }
    }
}
