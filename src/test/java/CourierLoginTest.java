import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class CourierLoginTest {
    private static CourierGenerator.Courier testCourier;

    @BeforeAll
    public static void setup() {
        testCourier = CourierGenerator.createTestCourier(CourierGenerator.BASE_URL);
        baseURI = CourierGenerator.BASE_URL;
    }

    @Test
    public void testSuccessfulCourierLogin() {
        testCourier.id = given()
                .log().all()
                .contentType("application/json")
                .body(Map.of(
                        "login", testCourier.login,
                        "password", testCourier.password
                ))
                .post("/api/v1/courier/login")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    public void testLoginWithWrongCredentials() {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "login", "Неправильный_логин",
                        "password", testCourier.password
                ))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void testLoginWithWrongPassword() {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "login", testCourier.login,
                        "password", "Неправильный_пароль"
                ))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void testLoginWithoutLoginField() {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "password", testCourier.password
                ))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void testLoginWithoutPasswordField() {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "login", testCourier.login
                ))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }//Тут баг?

    @Test
    public void testLoginNonExistentCourier() {
        String random = CourierGenerator.getRandomNano();
        given()
                .contentType("application/json")
                .body(Map.of(
                        "login", "Каштанов_" + random,
                        "password", "пароль"
                ))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterAll
    public static void cleanup() {
        if (testCourier.id != null) {
            given()
                    .contentType("application/json")
                    .delete("/api/v1/courier/" + testCourier.id)
                    .then()
                    .statusCode(200);
        }
    }
}