import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import trigon_module.Trigonometry;

public class TrigonometryTest {

  private static double eps;
  private static Trigonometry tr;

  @BeforeAll
  static void init() {
    eps = 0.0001;
    tr = new Trigonometry(eps);
  }

  @Test
  public void forceBurst() {
    for (int x = -200; x < 200; x ++) {
      assertAll(((double)x) / 100);
    }
  }

  @ParameterizedTest
  @ValueSource(doubles = {-10.0, -1.01, -1, -0.99, -0.01, 0, 10.0, 1.01, 1, 0.99, 0.01, Double.NaN,
      Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
  public void generalTest(double param) {
    assertAll(param);
  }

  private void assertAll(double param) {

    Assertions.assertEquals(Math.sin(param), tr.sin(param), eps);
    Assertions.assertEquals(Math.cos(param), tr.cos(param), eps);
    Assertions.assertEquals(Math.tan(param), tr.tan(param), eps);
    Assertions.assertEquals(1 / Math.cos(param), tr.sec(param), eps);
    Assertions.assertEquals(1 / Math.tan(param), tr.cot(param), eps);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-0.99, -0.01, 0})
  public void symmetricalTest(double param) {
    Assertions.assertEquals(Math.sin(param), -tr.sin(-param), eps);
    Assertions.assertEquals(Math.cos(param), tr.cos(-param), eps);
    Assertions.assertEquals(Math.tan(param), -tr.tan(-param), eps);
    Assertions.assertEquals(1 / Math.cos(param), tr.sec(-param), eps);
    Assertions.assertEquals(1 / Math.tan(param), -tr.cot(-param), eps);
  }
}