import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Map;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class CourierLoginTest extends BaseTest {
    private static String testCourierLogin;
    private static String testCourierPassword;

    @BeforeAll
    @Step("Создание тестового курьера для проверки авторизации")
    public static void setupTestData() {
        Map<String, String> courierData = CourierTestDataGenerator.generateTestCourierData();
        CourierApiClient.createCourier(CourierGenerator.BASE_URL, courierData)
                .then()
                .statusCode(201);

        testCourierLogin = courierData.get("login");
        testCourierPassword = courierData.get("password");
    }

    @Test
    @Step("Успешная авторизация курьера")
    @DisplayName("Успешная авторизация с валидными учетными данными")
    public void testSuccessfulCourierLogin() {
        Map<String, String> loginData = CourierTestDataGenerator.generateLoginData(
                testCourierLogin, testCourierPassword
        );

        int courierId = CourierApiClient.loginCourier(CourierGenerator.BASE_URL, loginData)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");

        CourierApiClient.deleteCourier(CourierGenerator.BASE_URL, courierId)
                .then()
                .statusCode(200);
    }

    @Test
    @Step("Авторизация с неверным логином")
    @DisplayName("Авторизация с неверным логином и валидным паролем")
    public void testLoginWithWrongCredentials() {
        Map<String, String> wrongLoginData = CourierTestDataGenerator.generateWrongLoginData(testCourierPassword);

        CourierApiClient.loginCourier(CourierGenerator.BASE_URL, wrongLoginData)
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Авторизация с неверным паролем")
    @DisplayName("Авторизация с валидным логином и неверным паролем")
    public void testLoginWithWrongPassword() {
        Map<String, String> wrongPasswordData = CourierTestDataGenerator.generateWrongPasswordData(testCourierLogin);

        CourierApiClient.loginCourier(CourierGenerator.BASE_URL, wrongPasswordData)
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Авторизация без указания логина")
    @DisplayName("Авторизация без обязательного поля 'login'")
    public void testLoginWithoutLoginField() {
        Map<String, String> withoutLoginData = CourierTestDataGenerator.generateLoginWithoutLoginData(testCourierPassword);

        CourierApiClient.loginCourier(CourierGenerator.BASE_URL, withoutLoginData)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Авторизация без указания пароля")
    @DisplayName("Авторизация без обязательного поля 'password'")
    public void testLoginWithoutPasswordField() {
        Map<String, String> withoutPasswordData = CourierTestDataGenerator.generateLoginWithoutPasswordData(testCourierLogin);

        CourierApiClient.loginCourier(CourierGenerator.BASE_URL, withoutPasswordData)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Авторизация несуществующего курьера")
    @DisplayName("Авторизация с несуществующими учетными данными")
    public void testLoginNonExistentCourier() {
        Map<String, String> nonExistentData = CourierTestDataGenerator.generateNonExistentCourierData();

        CourierApiClient.loginCourier(CourierGenerator.BASE_URL, nonExistentData)
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterAll
    @Step("Удаление тестового курьера после выполнения всех тестов")
    public static void cleanup() {
        try {
            Map<String, String> loginData = CourierTestDataGenerator.generateLoginData(
                    testCourierLogin, testCourierPassword
            );

            int courierId = CourierApiClient.loginCourier(CourierGenerator.BASE_URL, loginData)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("id");

            CourierApiClient.deleteCourier(CourierGenerator.BASE_URL, courierId)
                    .then()
                    .statusCode(200);

            System.out.println("Тестовый курьер успешно удален: " + testCourierLogin);
        } catch (Exception e) {
            System.out.println("Курьер уже удален или недоступен: " + e.getMessage());
        }
    }
}