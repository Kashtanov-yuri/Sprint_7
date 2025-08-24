import java.time.LocalDateTime;
import java.util.Map;

public class CourierGenerator {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    public static String getRandomNano() {
        return String.valueOf(LocalDateTime.now().getNano());
    }

    public static Map<String, String> generateValidCourierData() {
        String random = getRandomNano();
        return Map.of(
                "login", "Юрий_" + random,
                "password", "theEasiestPassword",
                "firstName", "Каштанов"
        );
    }

    public static Map<String, String> generateCourierWithoutLogin() {
        return Map.of(
                "password", "somePassword",
                "firstName", "Иван"
        );
    }

    public static Map<String, String> generateCourierWithoutPassword() {
        return Map.of(
                "login", "userWithoutPassword_" + getRandomNano(),
                "firstName", "Петр"
        );
    }

    public static Map<String, String> generateCourierWithoutFirstName() {
        return Map.of(
                "login", "Юрий2_" + getRandomNano(),
                "password", "somePassword"
        );
    }
}