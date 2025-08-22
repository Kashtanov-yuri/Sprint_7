import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class OrderVerificationTest extends BaseTest {

    @Test
    @Step("Тест получения списка заказов")
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