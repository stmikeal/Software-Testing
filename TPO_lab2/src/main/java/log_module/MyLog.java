package log_module;

import java.io.PrintWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

public class MyLog {

    private static MyLn ln = new MyLn();
    private double eps = 0.001;

    public MyLog(MyLn ln) {
       MyLog.ln = ln;
    }

    public MyLog(double eps) {
        this.eps = eps;
    }

    public void setEps(double x) {
        this.eps = x;
    }

    public double ln(double x) {
        return ln.calculate(x);
    }

    public double log(double arg, double base) {
        return ln(arg)/ln(base);
    }

    public void writeCSV(double x, PrintWriter out) {
        try {
            CSVPrinter printer = CSVFormat.DEFAULT.print(out);
            printer.printRecord(x, ln(x), log(x, 2), log(x, 3), log(x, 10));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
