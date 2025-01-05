import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Tunnel {

  public static final int TUNNEL_WIDTH = 5;
  public static final int TRAVEL_TIME = 1000;

  private final ExecutorService executorService;
  private final AtomicInteger shipCounter = new AtomicInteger(0);

  public Tunnel() {
    this.executorService = Executors.newFixedThreadPool(TUNNEL_WIDTH);
  }


  /**
   * @param cargoType
   * @param capacity  Создаём объект класса {@link Ship} с указанными параметрами. Затем спим в
   *                  потоке {@link Tunnel#TRAVEL_TIME} мс и отправляем корабль на загрузку вызовом
   *                  метода {@link Ship#sendForLoading()}
   */
  public void addShip(CargoType cargoType, CapacityType capacity) {
    Ship ship = new Ship(cargoType, capacity, shipCounter.incrementAndGet());
    System.out.println(
        "Ship " + ship.getId() + " with cargo type " + cargoType + " and " + capacity + " capacity "
            + " is entering the tunnel");
    executorService.execute(() -> { // Запускаем проход через туннель в отдельном потоке
      try {
        Thread.sleep(TRAVEL_TIME);
        ship.sendForLoading();
        System.out.println("Ship " + ship.getId() + " has exited the tunnel");
      } catch (InterruptedException e) {
        throw new RuntimeException(
            "An error occurred while the ship " + ship.getId() +
                " was passing through the tunnel", e);
      }
    });
  }

  public void addShip() {
    addShip(CargoType.getRandomCargoType(),
        CapacityType.getRandomCapacityType());
  }

  public void close() {
    executorService.shutdown();
  }
}
