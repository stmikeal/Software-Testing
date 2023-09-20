import java.io.PrintWriter;
import log_module.MyLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import trigon_module.Trigonometry;

public class MyFunction {

  private final Trigonometry tr;
  private final MyLog log;
  private double eps = 0.001;

  public void setEps(double x) {
    this.eps = x;
    tr.setEps(eps);
    log.setEps(eps);
  }
  public MyFunction() {
    tr = new Trigonometry(eps);
    log = new MyLog(eps);
  }

  public MyFunction(Trigonometry tr, MyLog log) {
    this.tr = tr;
    this.log = log;
  }

  public MyFunction(double eps) {
    this();
    setEps(eps);
  }

  public double calculate(double x) {
    if (x <= 0) {
      return ((((Math.pow(tr.cos(x) / tr.tan(x), 3) * tr.sin(x)) / tr.sec(x))
          * ((tr.cos(x) - (tr.tan(x) * tr.sec(x))) / (tr.sec(x) + (tr.cos(x) * tr.cot(x))))));
    } else {
      return (((((log.log(x, 10) - log.log(x, 2)) * log.log(x, 2)) + (log.log(x, 10) / log.ln(x)))
          + log.log(x, 2)) + ((log.log(x, 10) / log.log(x, 2)) / log.log(x, 3)));
    }
  }

  public void writeCSV(double x, PrintWriter out) {
    double res = calculate(x);
    try{
      CSVPrinter printer = CSVFormat.DEFAULT.print(out);
      printer.printRecord(x, res);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
