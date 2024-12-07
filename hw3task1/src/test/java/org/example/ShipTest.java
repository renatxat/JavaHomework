package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShipTest {

  @Test
  void run() {
    Ship ship = new Ship(CargoType.BREAD, CapacityType.LARGE, 1);
    ship.run();
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