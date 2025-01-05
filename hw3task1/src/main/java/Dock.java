import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Это причал для погрузки грузов. Грузит груз со скоростью LOAD_COEFFICIENT единиц в секунду.
 * Одновременно грузится ровно один {@link Ship}
 */
public class Dock {

  public static final int LOAD_COEFFICIENT = 10;
  public static final int ONE_SECOND = 1000;


  private final CargoType cargoType;

  public CargoType getCargoType() {
    return cargoType;
  }

  private final ExecutorService executorService;

  public Dock(CargoType cargoType) {
    this.cargoType = cargoType;
    executorService = Executors.newSingleThreadExecutor();
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
        // Имитация загрузки товара
        Thread.sleep((long) ONE_SECOND * ship.getCapacity().get() / LOAD_COEFFICIENT);
        System.out.println("Ship " + ship.getId() + " is fully loaded");
      } catch (InterruptedException e) {
        throw new RuntimeException(
            "An error occurred while the ship " + ship.getId() + " was loading at the dock with"
                + cargoType, e);
      }
    });
  }

  public void close() {
    executorService.shutdown();
  }
}
