package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DockTest {

  private Dock dock;

  @BeforeEach
  void setUp() {
    dock = new Dock(CargoType.BANANA);
  }

  @Test
  void testLoadCargoWithoutException() throws InterruptedException {

    for (var capacityType : CapacityType.values()) {
      Ship ship = new Ship(CargoType.BANANA, capacityType, 1);
      dock.loadCargo(ship);

      ExecutorService executor = Executors.newSingleThreadExecutor();
      Future<?> future = executor.submit(() -> dock.loadCargo(ship));
      // Дождаться завершения загрузки
      try {
        future.get();
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
      // Проверяем, что загрузка прошла успешно (тестируем только отсутствие исключений)
      assertTrue(true);
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
    Ship ship1 = new Ship(CargoType.BANANA, CapacityType.SMALL, 1);
    Ship ship2 = new Ship(CargoType.BANANA, CapacityType.LARGE, 2);

    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future<?> future1 = executor.submit(() -> dock.loadCargo(ship1));
    Future<?> future2 = executor.submit(() -> dock.loadCargo(ship2));

    // Дождаться завершения загрузки обоих кораблей
    future1.get();
    future2.get();

    // Проверяем, что загрузка прошла успешно (тестируем только отсутствие исключений)
    assertTrue(true);

    executor.shutdown();
  }
}
