package trigon_module;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

public class MySin {

  private static double DELTA = 0.001;

  public double calculate(double x) {
    return calculate(x, DELTA);
  }

  public MySin(double eps) {
    DELTA = eps;
  }

  public MySin() {}

  public double calculate(double x, double eps) {
    if (x == Double.NEGATIVE_INFINITY || x == Double.POSITIVE_INFINITY || Double.isNaN(x)) {
      return Double.NaN;
    } else if (x > 0) {
      while (x > Math.PI) {
        x -= 2 * Math.PI;
      }
    } else if (x < 0) {
      while (x < -Math.PI) {
        x += 2 * Math.PI;
      }
    }

    double res = x, term = x;
    double divisible = x * x * x;
    long divider = 6, sign = -1, pow = 3;

    while (Math.abs(term + sign * divisible / divider) > eps) {
      term = sign * (divisible / divider);
      res += term;

      sign *= -1;
      divisible *= x * x;
      divider = divider * ++pow * ++pow;
    }

    return res;
  }

  public double writeCSV(double x, Writer out) {
    double res = calculate(x);
    double tanArg = -Math.abs(x - Math.PI * Math.floor((x + Math.PI/2) / Math.PI));
    try {
      CSVPrinter printer = CSVFormat.DEFAULT.print(out);
      printer.printRecord(x, res, calculate(Math.PI/2 + x), calculate(tanArg), calculate(Math.PI/2 + tanArg));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return res;
  }

}
