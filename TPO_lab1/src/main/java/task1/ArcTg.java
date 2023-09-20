package task1;

public class ArcTg {

  /**
   * Разбиение arctg в ряд Маклорена.
   * arctg = x - x^3/3 + x^5/5 ...
   * В отдельности рассмотрен случай |x| > 1, потому что ряд Маклорена в таком случае не сходится.
   * Для этого используется математическое равенство arctg(x) + arctg(1/x) = PI/2.
   * (в случае отрицательных значений, очевидно, знаки инвертируются).
   * @param x аргумент функции
   * @return значение функции
   */
  public static double calculateArctg(double x){

    if (Double.isNaN(x)) return Double.NaN;

    boolean isGreater = false;
    if (Math.abs(x) > 1) {
      isGreater = true;
      x = 1/x;
    }

    double ans = x;
    double tmp = 1;
    double divider = 3;
    int sign = -1;

    while (Math.abs(tmp) > 0.0001){
      tmp = sign * Math.pow(x, divider) / divider;
      ans += tmp;
      divider += 2;
      sign *= -1;
    }

    return isGreater ? Math.copySign(Math.PI/2, x) - ans : ans;
  }

}

