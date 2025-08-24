import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(AllureJunit5.class)
public class CreatingCourierTest extends BaseTest {

    @Test
    @Step("Успешное создание курьера")
    @DisplayName("Создание курьера с валидными данными")
    public void testSuccessfulCourierCreation() {
        Map<String, String> validCourier = generateValidCourierData();
        createCourierAndVerify(validCourier);
        deleteCourierAfterTest(validCourier);
    }

    @Test
    @Step("Создание дубликата курьера")
    @DisplayName("Попытка создания курьера с уже существующим логином")
    public void testDuplicateCourierCreation() {
        Map<String, String> validCourier = generateValidCourierData();
        createCourier(validCourier);
        attemptDuplicateCreation(validCourier);
        deleteCourierAfterTest(validCourier);
    }

    @Test
    @Step("Создание курьера без логина")
    @DisplayName("Создание курьера без обязательного поля 'login'")
    public void testCourierCreationWithoutLogin() {
        Map<String, String> courierWithoutLogin = generateCourierWithoutLogin();
        attemptCreationWithoutRequiredField(courierWithoutLogin);
    }

    @Test
    @Step("Создание курьера без пароля")
    @DisplayName("Создание курьера без обязательного поля 'password'")
    public void testCourierCreationWithoutPassword() {
        Map<String, String> courierWithoutPassword = generateCourierWithoutPassword();
        attemptCreationWithoutRequiredField(courierWithoutPassword);
    }

    @Test
    @Step("Создание курьера без имени")
    @DisplayName("Создание курьера без необязательного поля 'firstName'")
    public void testCourierCreationWithoutFirstName() {
        Map<String, String> courierWithoutFirstName = generateCourierWithoutFirstName();
        createCourierAndVerify(courierWithoutFirstName);
        deleteCourierAfterTest(courierWithoutFirstName);
    }

    @Step("Генерация валидных данных курьера")
    private Map<String, String> generateValidCourierData() {
        return CourierGenerator.generateValidCourierData();
    }

    @Step("Генерация данных курьера без логина")
    private Map<String, String> generateCourierWithoutLogin() {
        return CourierGenerator.generateCourierWithoutLogin();
    }

    @Step("Генерация данных курьера без пароля")
    private Map<String, String> generateCourierWithoutPassword() {
        return CourierGenerator.generateCourierWithoutPassword();
    }

    @Step("Генерация данных курьера без имени")
    private Map<String, String> generateCourierWithoutFirstName() {
        return CourierGenerator.generateCourierWithoutFirstName();
    }

    @Step("Создание курьера и проверка успешности")
    private void createCourierAndVerify(Map<String, String> courierData) {
        CourierApiClient.createCourier(CourierGenerator.BASE_URL, courierData)
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Создание курьера")
    private void createCourier(Map<String, String> courierData) {
        CourierApiClient.createCourier(CourierGenerator.BASE_URL, courierData)
                .then()
                .statusCode(201);
    }

    @Step("Попытка создания дубликата курьера")
    private void attemptDuplicateCreation(Map<String, String> originalCourier) {
        Map<String, String> duplicateCourier = Map.of(
                "login", originalCourier.get("login"),
                "password", "anotherPassword",
                "firstName", "Каштанов"
        );

        CourierApiClient.createCourier(CourierGenerator.BASE_URL, duplicateCourier)
                .then()
                .log().all()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Попытка создания без обязательного поле")
    private void attemptCreationWithoutRequiredField(Map<String, String> invalidCourierData) {
        CourierApiClient.createCourier(CourierGenerator.BASE_URL, invalidCourierData)
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Удаление курьера после теста")
    private void deleteCourierAfterTest(Map<String, String> courierData) {
        CourierApiClient.deleteCourierByCredentials(
                CourierGenerator.BASE_URL,
                courierData.get("login"),
                courierData.get("password")
        );
    }
}