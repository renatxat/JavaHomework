import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShipTest {

  Ship ship;

  ShipTest() {
    ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
  }

  @Test
  void sendForLoading() {
    ship.sendForLoading();
  }

  @Test
  void getCapacity() {
    assertEquals(CapacityType.LARGE, ship.getCapacity());
  }

  @Test
  void getCargoType() {
    assertEquals(CargoType.BREAD, ship.getCargoType());
  }

  @Test
  void getId() {
    assertEquals(1, ship.getId());
  }

}