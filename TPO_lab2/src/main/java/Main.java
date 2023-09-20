import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

import log_module.MyLn;
import log_module.MyLog;
import trigon_module.MySin;
import trigon_module.Trigonometry;

public class Main {

  private static final class State<T> {
    T obj;
    PrintWriter writer;
    double x;
    public State(T obj, PrintWriter writer, double x) {
      this.obj = obj;
      this.writer = writer;
      this.x = x;
    }
  }
  private static final String dirPathIn = "src/main/resources/CSVFiles/input/";
  private static final String dirPathOut = "src/main/resources/CSVFiles/output/";
  private static final Map<Class<?>, Consumer<State<?>>> procedures = new HashMap<>();
  static {
    procedures.put(MyFunction.class, (state) -> ((MyFunction)state.obj).writeCSV(state.x, state.writer));
    procedures.put(MyLn.class, (state) -> ((MyLn)state.obj).writeCSV(state.x, state.writer));
    procedures.put(MyLog.class, (state) -> ((MyLog)state.obj).writeCSV(state.x, state.writer));
    procedures.put(MySin.class, (state) -> ((MySin)state.obj).writeCSV(state.x, state.writer));
    procedures.put(Trigonometry.class, (state) -> ((Trigonometry)state.obj).writeCSV(state.x, state.writer));
  }
  private static final Map<Class<?>, Function<Double, Object>> objects = new HashMap<>();
  static {
    objects.put(MyFunction.class, MyFunction::new);
    objects.put(MyLn.class, MyLn::new);
    objects.put(MyLog.class, MyLog::new);
    objects.put(MySin.class, MySin::new);
    objects.put(Trigonometry.class, Trigonometry::new);
  }

  public static void main(String[] args) {
    for (Class<?> curClass : procedures.keySet()) {
      String inPath = dirPathIn + curClass.getName() + "In.csv";
      String outPath = dirPathOut + curClass.getName() + "Out.csv";
      File f = new File(inPath);

      try (Scanner scanner = new Scanner(f); PrintWriter writer = new PrintWriter(outPath, StandardCharsets.UTF_8)) {
        scanner.useDelimiter(",");
        int start = scanner.nextInt();
        int end = scanner.nextInt();
        int step = scanner.nextInt();
        double eps = Double.parseDouble(scanner.next());
        for (int x = start; x <= end; x++) {
          procedures.get(curClass).accept(new State<>(objects.get(curClass).apply(eps), writer, ((double) x) / step));
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
