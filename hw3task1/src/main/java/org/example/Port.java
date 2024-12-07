package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadLocalRandom;

public class Port {

  private final ExecutorService executorService;
  private final AtomicInteger shipCounter = new AtomicInteger(0);

  public Port() {
    this.executorService = Executors.newFixedThreadPool(5);
  }

  public void AddShip(CargoType cargoType, CapacityType capacity) {
    Ship ship = new Ship(cargoType, capacity, shipCounter.incrementAndGet());
    System.out.println(
        "Ship " + ship.getId() + " with cargo type " + cargoType + " and " + capacity + " capacity "
            + " is entering the tunnel");
    executorService.execute(() -> {
      try {
        Thread.sleep(1000); // Проход через туннель
        ship.run(); // В отдельном потоке запускается, никому не мешает
        System.out.println("Ship " + ship.getId() + " has exited the tunnel");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void AddShip() {
    CargoType cargoType = CargoType.values()[ThreadLocalRandom.current()
        .nextInt(CargoType.values().length)];
    CapacityType capacity = CapacityType.values()[ThreadLocalRandom.current()
        .nextInt(CapacityType.values().length)];
    AddShip(cargoType, capacity);
  }

  public void close() {
    executorService.shutdown();
  }
}
