package ru.netology;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class DataGenerator {
    @UtilityClass
    public static class LogIn {
        /*
        public static String GenerateStatus() {
            final String[] statuses = {"active", "blocked"};

            public String getRandomElement() {
                return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
            }
        }
        */

        public static DataClass[] generateUsers() {
            Faker faker = new Faker (new Locale("en"));
            DataClass[] users = new DataClass[2];
            // String status = LogIn.GenerateStatus();

            users[0] = new DataClass(
                    faker.name().username(),
                    faker.internet().password(),
                    "active"
            );
            users[1] = new DataClass(
                    faker.name().username(),
                    faker.internet().password(),
                    "blocked"
            );
            return users;
        }
    }
}
