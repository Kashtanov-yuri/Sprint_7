import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class OrderApiClient extends BaseTest  {

    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    private static final String GET_ORDERS_ENDPOINT = "/api/v1/orders";

    @Step("Создание заказа с данными: {orderData}")
    public static Response createOrder(Map<String, Object> orderData) {
        return given()
                .log().all()
                .contentType("application/json")
                .baseUri(CourierGenerator.BASE_URL)
                .body(orderData)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }

    @Step("Получение списка всех заказов")
    public static Response getOrders() {
        return given()
                .baseUri(CourierGenerator.BASE_URL)
                .when()
                .get(GET_ORDERS_ENDPOINT);
    }
}
