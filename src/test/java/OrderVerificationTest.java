import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class OrderVerificationTest {

    @BeforeAll
    public static void setup() {
        baseURI = CourierGenerator.BASE_URL;
    }

    @Test
    public void testOrdersListIsReturned() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", is(notNullValue()))
                .body("orders", is(not(empty())));
    }
}