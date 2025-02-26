package nextstep.subway.line.domain;

import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    @ParameterizedTest
    @CsvSource({
            "1000, 5, 0, 0",
            "10, 20, 0, 1250", "10, 17, 0, 720", "10, 6, 0, 450",
            "18, 20, 0, 1450", "18, 17, 0, 880", "18, 6, 0, 550",
            "10, 20, 900, 2150", "10, 17, 900, 1440", "10, 6, 900, 900",
            "12, 20, 900, 2250", "12, 17, 900, 1520", "12, 6, 900, 950",
            "50, 20, 0, 2050", "50, 17, 0, 1360", "50, 10, 0, 850",
            "58, 20, 0, 2150", "58, 17, 0, 1440", "58, 10, 0, 900",
            "178, 20, 0, 3650", "178, 17, 0, 2640", "178, 10, 0, 1650"
    })
    void calculate(int distance, int age, int surcharge, int expected) {
        System.out.println(expected);
        System.out.println(Fare.calculate(new Distance(distance), age, surcharge).toInt());
        assertThat(Fare.calculate(new Distance(distance), age, surcharge)).isEqualTo(new Fare(expected));
    }
}
