package org.example;

public class Ship implements Runnable {

  private final int id;
  private final CargoType cargoType;
  private final CapacityType capacity;

  public Ship(CargoType cargoType, CapacityType capacity, int id) {
    this.id = id;
    this.cargoType = cargoType;
    this.capacity = capacity;
  }

  @Override
  public void run() {
    try {
      DockManager dockManager = DockManager.getInstance();
      Dock dock = dockManager.getDock(cargoType);
      dock.loadCargo(this); // Погрузка товара
    } catch (Exception e) {
      throw e;
    }
  }

  public CapacityType getCapacity() {
    return capacity;
  }

  public CargoType getCargoType() {
    return cargoType;
  }

  public int getId() {
    return id;
  }
}
