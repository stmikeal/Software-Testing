import log_module.MyLn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LnTest {
    private static double DELTA;
    private static MyLn log;

    @BeforeAll
    static void init() {
        DELTA = 0.0001;
        log = new MyLn(DELTA);
    }

    @Test
    public void forceBurst() {
        for (int x = 0; x < 1000; x ++) {
            assertion(((double)x) / 100);
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {1, 2, 3, 5, 10, 0.1})
    public void generalTest(double x) {
        assertion(x);
    }

    public void assertion(double x) {
        Assertions.assertEquals(Math.log(x), log.calculate(x, DELTA), DELTA);
    }
}
