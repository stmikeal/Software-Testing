package task1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ArcTgTest {

    private static double DELTA;

    @BeforeAll
    static void init() {
        DELTA = 0.0001;
    }

    /**
     * Тестирование методом граничных значений.
     * @param param очередное значение функции
     */
    @ParameterizedTest
    @ValueSource(doubles = {-10.0, -1.01, -1, -0.99, -0.01, 0, 10.0, 1.01, 1, 0.99, 0.01, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    public void generalTest(double param) {
        Assertions.assertEquals(Math.atan(param), ArcTg.calculateArctg(param), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.99, -0.01, 0})
    public void symmetricalTest(double param) {
        Assertions.assertEquals(ArcTg.calculateArctg(param), -1 * ArcTg.calculateArctg(-1 * param), DELTA);
    }
}
