import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class CourierApiClient {
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = COURIER_PATH + "/login";

    @Step("Создание курьера")
    public static Response createCourier(String baseUrl, Map<String, String> courierData) {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(courierData)
                .post(COURIER_PATH);
    }

    @Step("Авторизация курьера")
    public static Response loginCourier(String baseUrl, Map<String, String> loginData) {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(loginData)
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера с ID: {courierId}")
    public static Response deleteCourier(String baseUrl, int courierId) {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .delete(COURIER_PATH + "/" + courierId);
    }

    @Step("Полное удаление курьера по логину и паролю")
    public static void deleteCourierByCredentials(String baseUrl, String login, String password) {
        try {
            Map<String, String> loginData = Map.of("login", login, "password", password);

            int courierId = loginCourier(baseUrl, loginData)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("id");

            deleteCourier(baseUrl, courierId)
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            System.out.println("Не удалось удалить курьера: " + e.getMessage());
        }
    }
}