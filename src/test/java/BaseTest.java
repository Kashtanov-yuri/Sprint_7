import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.baseURI;

@ExtendWith(AllureJunit5.class)
public class BaseTest {

    @BeforeAll
    public static void setup() {
        baseURI = CourierGenerator.BASE_URL;
        System.out.println("Base URI установлен: " + baseURI);
    }
}
