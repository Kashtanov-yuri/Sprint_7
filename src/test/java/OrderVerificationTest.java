import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class OrderVerificationTest extends BaseTest {

    @Test
    @Step("Тест получения списка заказов")
    @DisplayName("Получение списка всех заказов")
    public void testOrdersListIsReturned() {
        OrderApiClient.getOrders()
                .then()
                .statusCode(200)
                .body("orders", is(notNullValue()))
                .body("orders", is(not(empty())));
    }
}