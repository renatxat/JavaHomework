package hw3task1;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DockTest {

  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;
  final private int EPS = 100;
  private Dock dock;


  @BeforeEach
  void setUp() {
    dock = new Dock(CargoType.BANANA);
    // Сохраняем старый стандартный вывод
    this.originalOut = System.out;
    // Создаем поток для захвата вывода
    this.outputStream = new ByteArrayOutputStream();
    PrintStream newOut = new PrintStream(outputStream);
    // Перенаправляем стандартный вывод
    System.setOut(newOut);
  }


  double getPredictTime(int capacity) {
    return (double) Dock.ONE_SECOND * (double) capacity / (double) Dock.LOAD_COEFFICIENT;
  }


  @Test
  void testLoadCargoWithoutException() throws InterruptedException {
    for (var capacityType : CapacityType.values()) {
      final double time = System.currentTimeMillis();
      outputStream.reset();

      Ship ship = new Ship(CargoType.BANANA, capacityType, 1);

      ExecutorService executor = Executors.newSingleThreadExecutor();
      Future<?> future = executor.submit(() -> dock.loadCargo(ship));
      // Дождаться завершения загрузки
      try {
        future.get();
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      }

      // Проверяем соответствие времени загрузки
      String output;
      do {
        output = outputStream.toString();
      } while (!output.contains(" is fully loaded"));

      assertTrue(abs(System.currentTimeMillis() - time - getPredictTime(ship.getCapacity().get()))
          < EPS);
      executor.shutdown();
    }
  }


  @Test
  void testLoadCargoWithException() throws InterruptedException {
    for (var cargoType : CargoType.values()) {
      if (cargoType == CargoType.BANANA) {
        continue;
      }
      for (var capacityType : CapacityType.values()) {
        Ship ship = new Ship(cargoType, capacityType, 1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> dock.loadCargo(ship));
        try {
          future.get();
        } catch (ExecutionException e) {
          // Проверяем, что загрузка не прошла из-за несовпадения типа груза
          assertInstanceOf(IllegalArgumentException.class, e.getCause());
          executor.shutdown();
          continue;
        }
        fail("Ожидалось исключение из-за несовпадения типа груза");
      }
    }
  }


  @Test
  void testLoadCargoConcurrency() throws InterruptedException, ExecutionException {
    outputStream.reset();
    final double time = System.currentTimeMillis();

    Ship ship1 = new Ship(CargoType.BANANA, CapacityType.SMALL, 1);
    Ship ship2 = new Ship(CargoType.BANANA, CapacityType.LARGE, 2);

    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future<?> future1 = executor.submit(() -> dock.loadCargo(ship1));
    Future<?> future2 = executor.submit(() -> dock.loadCargo(ship2));

    // Дождаться завершения загрузки обоих кораблей
    future1.get();
    future2.get();

    // Проверяем соответствие времени загрузки
    int numberOfLines = 0;
    while (numberOfLines < 2) {
      String output = outputStream.toString();
      numberOfLines = output.split(" is fully loaded", -1).length - 1;
      // Подсчитываем количество созданных кораблей
    }

    assertTrue(abs(System.currentTimeMillis() - time - getPredictTime(ship1.getCapacity().get())
        - getPredictTime(ship2.getCapacity().get())) < EPS);

    executor.shutdown();
  }


  @AfterEach
  public void tearDown() {
    System.setOut(this.originalOut);
  }
}
