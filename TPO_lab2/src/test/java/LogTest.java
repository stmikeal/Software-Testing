import log_module.MyLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LogTest {

    private static double DELTA;
    private static MyLog log;

    @BeforeAll
    static void init() {
        DELTA = 0.0001;
        log = new MyLog(DELTA);
    }

    @Test
    public void forceBurst() {
        for (int x = 0; x < 1000; x ++) {
            assertion(((double)x) / 100, 2);
            assertion(((double)x) / 100, 3);
            assertion(((double)x) / 100, 5);
            assertion(((double)x) / 100, 10);
        }
    }

    public void assertion(double x, double base) {
        Assertions.assertEquals(Math.log(x)/Math.log(base), log.log(x, base), DELTA);
    }
}
