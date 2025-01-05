public class Ship {

  private final int id;
  private final CargoType cargoType;
  private final CapacityType capacity;

  public Ship(CargoType cargoType, CapacityType capacity, int id) {
    this.id = id;
    this.cargoType = cargoType;
    this.capacity = capacity;
  }

  public void sendForLoading() {
    DockManager dockManager = DockManager.getInstance();
    Dock dock = dockManager.getDock(cargoType);
    dock.loadCargo(this); // Погрузка товара
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
