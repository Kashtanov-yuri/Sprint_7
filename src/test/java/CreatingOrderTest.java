import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class CreatingOrderTest {

    @BeforeAll
    public static void setup() {
        baseURI = CourierGenerator.BASE_URL;
    }

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
    public void testOrderCreationWithDifferentColors(List<String> colors) {
        Map<String, Object> orderData = Map.of(
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

        given()
                .log().all()
                .contentType("application/json")
                .body(orderData)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void testOrderCreationResponseStructure() {
        Map<String, Object> orderData = Map.of(
                "firstName", "Naruto",
                "lastName", "Uchiha",
                "address", "Konoha, 142 apt.",
                "metroStation", 4,
                "phone", "+7 800 355 35 35",
                "rentTime", 5,
                "deliveryDate", "2025-08-20",
                "comment", "Saske, come back to Konoha"
                // Без указания цвета
        );

        given()
                .log().all()
                .contentType("application/json")
                .body(orderData)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(201)
                .body("$", hasKey("track"));
    }
}
