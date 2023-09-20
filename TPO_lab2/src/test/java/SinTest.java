import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import trigon_module.MySin;

public class SinTest {
  private static double DELTA;
  private static MySin sin;

  @BeforeAll
  static void init() {
    DELTA = 0.0001;
    sin = new MySin(DELTA);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-10.0, -1.01, -1, -0.99, -0.01, 0, 10.0, 1.01, 1, 0.99, 0.01, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
  public void generalTest(double param) {
    Assertions.assertEquals(Math.sin(param), sin.calculate(param, DELTA), DELTA);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-0.99, -0.01, 0})
  public void symmetricalTest(double param) {
    Assertions.assertEquals(Math.sin(param), -1 * sin.calculate(-1 * param, DELTA), DELTA);
  }
}