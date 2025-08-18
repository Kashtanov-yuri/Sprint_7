import java.time.LocalDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierGenerator {
    public static String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    public static String getRandomNano() {
        return String.valueOf(LocalDateTime.now().getNano());
    }
    public static Courier createTestCourier(String baseUrl) {
        String random = String.valueOf(System.currentTimeMillis());
        String login = "courier_" + random;
        String password = "test1234";
        String firstName = "TestCourier";

        Map<String, String> courierData = Map.of(
                "login", login,
                "password", password,
                "firstName", firstName
        );

        given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(courierData)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        return new Courier(login, password, null);
    }

    public static class Courier {
        public final String login;
        public final String password;
        public Integer id;

        public Courier(String login, String password, Integer id) {
            this.login = login;
            this.password = password;
            this.id = id;
        }
    }
}