import log_module.MyLn;
import log_module.MyLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import trigon_module.MySin;
import trigon_module.Trigonometry;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class IntegrationTest {

    private static final double DELTA = 0.001;

    private static class Pair {
        public Pair(Object obj, CSVRecord record) {
            this.obj = obj;
            this.record = record;
        }

        public Object obj;
        public CSVRecord record;
    }

    private final static String dirPath = "/resources/CsvFiles/output/";
    private static final Map<Class<?>, Object> objects = new HashMap<>();
    static {
        objects.put(MyFunction.class, Mockito.mock(MyFunction.class));
        objects.put(MyLn.class, Mockito.mock(MyLn.class));
        objects.put(MyLog.class, Mockito.mock(MyLog.class));
        objects.put(MySin.class, Mockito.mock(MySin.class));
        objects.put(Trigonometry.class, Mockito.mock(Trigonometry.class));
    }

    private static final Map<Class<?>, Consumer<Pair>> procedures = new HashMap<>();
    static {
        procedures.put(MyFunction.class, (pair) -> Mockito.when(((MyFunction) pair.obj).calculate(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(1))));
        procedures.put(MyLn.class, (pair) -> Mockito.when(((MyLn) pair.obj).calculate(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(1))));
        procedures.put(MyLog.class, (pair) -> {
            Mockito.when(((MyLog) pair.obj).ln(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(1)));
            Mockito.when(((MyLog) pair.obj).log(Double.parseDouble(pair.record.get(0)), 2)).thenReturn(Double.parseDouble(pair.record.get(2)));
            Mockito.when(((MyLog) pair.obj).log(Double.parseDouble(pair.record.get(0)), 3)).thenReturn(Double.parseDouble(pair.record.get(3)));
            Mockito.when(((MyLog) pair.obj).log(Double.parseDouble(pair.record.get(0)), 10)).thenReturn(Double.parseDouble(pair.record.get(4)));
        });
        procedures.put(MySin.class, (pair) -> {
            double x = Double.parseDouble(pair.record.get(0));
            double tanArg = -Math.abs(x - Math.PI * Math.floor((x + Math.PI / 2) / Math.PI));
            Mockito.when(((MySin) pair.obj).calculate(x)).thenReturn(Double.parseDouble(pair.record.get(1)));
            Mockito.when(((MySin) pair.obj).calculate(x + Math.PI / 2)).thenReturn(Double.parseDouble(pair.record.get(2)));
            Mockito.when(((MySin) pair.obj).calculate(tanArg)).thenReturn(Double.parseDouble(pair.record.get(3)));
            Mockito.when(((MySin) pair.obj).calculate(tanArg + Math.PI / 2)).thenReturn(Double.parseDouble(pair.record.get(4)));
        });
        procedures.put(Trigonometry.class, (pair) -> {
            Mockito.when(((Trigonometry) pair.obj).sin(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(1)));
            Mockito.when(((Trigonometry) pair.obj).cos(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(2)));
            Mockito.when(((Trigonometry) pair.obj).tan(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(3)));
            Mockito.when(((Trigonometry) pair.obj).sec(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(4)));
            Mockito.when(((Trigonometry) pair.obj).cot(Double.parseDouble(pair.record.get(0)))).thenReturn(Double.parseDouble(pair.record.get(5)));
        });
    }

    @BeforeAll
    public static void init() {
        for (Class<?> mockClass : procedures.keySet()) {
            try (InputStream inputStream = IntegrationTest.class.getResourceAsStream(dirPath + mockClass.getName() + "Out.csv");
                 Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
                for (CSVRecord record : CSVFormat.DEFAULT.parse(reader)) {
                    procedures.get(mockClass).accept(new Pair(objects.get(mockClass), record));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resources/CSVFiles/output/MyFunctionOut.csv", encoding = "UTF-8")
    public void testFunctionWithMocks(double x, double f) {
        MyFunction function = new MyFunction((Trigonometry) objects.get(Trigonometry.class), (MyLog) objects.get(MyLog.class));
        Assertions.assertEquals(f, function.calculate(x), DELTA);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resources/CSVFiles/output/MyFunctionOut.csv", encoding = "UTF-8")
    public void testFunctionWithTrigan(double x, double f) {
        MyFunction function = new MyFunction(new Trigonometry((MySin) objects.get(MySin.class)), (MyLog) objects.get(MyLog.class));
        Assertions.assertEquals(f, function.calculate(x), DELTA);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resources/CSVFiles/output/MyFunctionOut.csv", encoding = "UTF-8")
    public void testFunctionWithLog(double x, double f) {
        MyFunction function = new MyFunction(new Trigonometry((MySin) objects.get(MySin.class)), new MyLog((MyLn) objects.get(MyLn.class)));
        Assertions.assertEquals(f, function.calculate(x), DELTA);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resources/CSVFiles/output/MyFunctionOut.csv", encoding = "UTF-8")
    public void testFunctionWithSin(double x, double f) {
        MyFunction function = new MyFunction(new Trigonometry(new MySin()), new MyLog((MyLn) objects.get(MyLn.class)));
        Assertions.assertEquals(f, function.calculate(x), DELTA);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resources/CSVFiles/output/MyFunctionOut.csv", encoding = "UTF-8")
    public void testFunctionWithLn(double x, double f) {
        MyFunction function = new MyFunction(new Trigonometry(new MySin()), new MyLog(new MyLn()));
        Assertions.assertEquals(f, function.calculate(x), DELTA);
    }
}
