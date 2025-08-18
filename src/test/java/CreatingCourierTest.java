import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(AllureJunit5.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreatingCourierTest {
    private static Map<String, String> validCourier;
    private static Map<String, String> duplicateCourier;
    private static String random;
    private static String createdCourierLogin;

    @BeforeAll
    public static void setup() {
        random = CourierGenerator.getRandomNano();
        validCourier = Map.of(
                "login", "Юрий1" + random,
                "password", "theEasiestPassword",
                "firstName", "Каштанов"
        );
        duplicateCourier = Map.of(
                "login", "Юрий1" + random,
                "password", "anotherPassword",
                "firstName", "Каштанов"
        );
        baseURI = CourierGenerator.BASE_URL;
        createdCourierLogin = null;
    }

    @Test
    @Order(1)
    public void testSuccessfulCourierCreation() {
        given()
                .log().all()
                .contentType("application/json")
                .body(validCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        createdCourierLogin = validCourier.get("login");
    }

    @Test
    @Order(2)
    public void testDuplicateCourierCreation() {
        given()
                .log().all()
                .contentType("application/json")
                .body(duplicateCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void testCourierCreationWithoutLogin() {
        Map<String, String> courierWithoutLogin = Map.of(
                "password", "somePassword",
                "firstName", "Иван"
        );

        given()
                .log().all()
                .contentType("application/json")
                .body(courierWithoutLogin)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCourierCreationWithoutPassword() {
        Map<String, String> courierWithoutPassword = Map.of(
                "login", "userWithoutPassword",
                "firstName", "Петр"
        );

        given()
                .log().all()
                .contentType("application/json")
                .body(courierWithoutPassword)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCourierCreationWithoutFirstName() {
        Map<String, String> courierWithoutFirstName = Map.of(
                "login", "Юрий2" + random,
                "password", "somePassword"
        );
        given()
                .log().all()
                .contentType("application/json")
                .body(courierWithoutFirstName)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @AfterAll
    public static void cleanup() {
        if (createdCourierLogin != null) {
            Integer courierId = given()
                    .contentType("application/json")
                    .body(Map.of(
                            "login", createdCourierLogin,
                            "password", validCourier.get("password")
                    ))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .extract()
                    .path("id");
            if (courierId != null) {
                given()
                        .when()
                        .delete("/api/v1/courier/" + courierId)
                        .then()
                        .statusCode(200);
            }
        }
    }
}