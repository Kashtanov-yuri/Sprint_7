import io.qameta.allure.Step;
import java.util.Map;

public class CourierTestDataGenerator {

    @Step("Генерация данных тестового курьера")
    public static Map<String, String> generateTestCourierData() {
        String random = String.valueOf(System.currentTimeMillis());
        return Map.of(
                "login", "courier_" + random,
                "password", "test1234",
                "firstName", "TestCourier"
        );
    }

    @Step("Генерация данных для авторизации")
    public static Map<String, String> generateLoginData(String login, String password) {
        return Map.of(
                "login", login,
                "password", password
        );
    }

    @Step("Генерация данных для авторизации с неверным логином")
    public static Map<String, String> generateWrongLoginData(String password) {
        return Map.of(
                "login", "Неправильный_логин",
                "password", password
        );
    }

    @Step("Генерация данных для авторизации с неверным паролем")
    public static Map<String, String> generateWrongPasswordData(String login) {
        return Map.of(
                "login", login,
                "password", "Неправильный_пароль"
        );
    }

    @Step("Генерация данных для авторизации без логина")
    public static Map<String, String> generateLoginWithoutLoginData(String password) {
        return Map.of("password", password);
    }

    @Step("Генерация данных для авторизации без пароля")
    public static Map<String, String> generateLoginWithoutPasswordData(String login) {
        return Map.of("login", login);
    }

    @Step("Генерация данных для несуществующего курьера")
    public static Map<String, String> generateNonExistentCourierData() {
        String random = String.valueOf(System.currentTimeMillis());
        return Map.of(
                "login", "Каштанов_" + random,
                "password", "пароль"
        );
    }
}
