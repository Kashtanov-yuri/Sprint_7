import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class CreatingOrderTest extends BaseTest {

    private static Stream<Arguments> provideColorTestData() {
        return Stream.of(
                Arguments.of(List.of("BLACK")),
                Arguments.of(List.of("GREY")),
                Arguments.of(List.of("BLACK", "GREY")),
                Arguments.of(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideColorTestData")
    @Step("Тест создания заказа с разными цветами: {colors}")
    @DisplayName("Параметризованный тест создания заказа с цветами")
    public void testOrderCreationWithDifferentColors(List<String> colors) {
        Map<String, Object> orderData = generateOrderData(colors);

        OrderApiClient.createOrder(orderData)
                .then()
                .log().all()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    @Step("Тест структуры ответа при создании заказа")
    @DisplayName("Проверка структуры ответа при успешном создании заказа")
    public void testOrderCreationResponseStructure() {
        Map<String, Object> orderData = generateBaseOrderData();

        OrderApiClient.createOrder(orderData)
                .then()
                .log().all()
                .statusCode(201)
                .body("$", hasKey("track"));
    }

    @Step("Генерация данных заказа с цветами: {colors}")
    private Map<String, Object> generateOrderData(List<String> colors) {
        return Map.of(
                "firstName", "Naruto",
                "lastName", "Uchiha",
                "address", "Konoha, 142 apt.",
                "metroStation", 4,
                "phone", "+7 800 355 35 35",
                "rentTime", 5,
                "deliveryDate", "2025-08-20",
                "comment", "Saske, come back to Konoha",
                "color", colors
        );
    }

    @Step("Генерация базовых данных заказа")
    private Map<String, Object> generateBaseOrderData() {
        return Map.of(
                "firstName", "Naruto",
                "lastName", "Uchiha",
                "address", "Konoha, 142 apt.",
                "metroStation", 4,
                "phone", "+7 800 355 35 35",
                "rentTime", 5,
                "deliveryDate", "2025-08-20",
                "comment", "Saske, come back to Konoha"
        );
    }
}
