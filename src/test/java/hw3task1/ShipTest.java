package hw3task1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShipTest {

  @Test
  void sendForLoading() {
    Ship ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
    ship.sendForLoading();
  }

  @Test
  void getCapacity() {
    Ship ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
    assertEquals(CapacityType.LARGE, ship.getCapacity());
  }

  @Test
  void getCargoType() {
    Ship ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
    assertEquals(CargoType.BREAD, ship.getCargoType());
  }

  @Test
  void getId() {
    Ship ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
    assertEquals(1, ship.getId());
  }

}