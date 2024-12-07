package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dock {

  private final CargoType cargoType;
  private final ExecutorService executorService;

  public Dock(CargoType cargoType) {
    this.cargoType = cargoType;
    executorService = Executors.newFixedThreadPool(1);
  }

  public void loadCargo(Ship ship) {
    if (ship.getCargoType() != cargoType) {
      throw new IllegalArgumentException("Invalid cargo type");
    }
    this.executorService.execute(() -> {
      System.out.println(
          "Dock for " + cargoType + " is loading ship " + ship.getId() + " with "
              + ship.getCapacity() + " capacity");
      try {
        Thread.sleep(ship.getCapacity().get() / 10); // Загрузка товара
        System.out.println("Ship " + ship.getId() + " is fully loaded");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  void close() {
    executorService.shutdown();
  }
}
