package org.example;

/**
 * Это менеджер причалов для погрузки, который предоставляет доступ к причалам по типу груза
 * Реализован с помощью потокобезопасный синглтона, чтобы можно было досдать нужный причал прямо
 * внутри класса Ship
 */
public class DockManager {

  private static volatile DockManager instance;

  private static final Dock[] docks = {
      new Dock(CargoType.BREAD),
      new Dock(CargoType.BANANA),
      new Dock(CargoType.CLOTHING)
  };

  private DockManager() {
  }

  public static DockManager getInstance() {
    if (instance == null) {
      synchronized (DockManager.class) {
        if (instance == null) {
          instance = new DockManager();
        }
      }
    }
    return instance;
  }

  // Метод для получения дока по типу груза
  public Dock getDock(CargoType cargoType) {
    if (instance == null) {
      throw new IllegalStateException("DockManager is not initialized");
    }
    return switch (cargoType) {
      case BREAD -> docks[0];
      case BANANA -> docks[1];
      case CLOTHING -> docks[2];
      default -> throw new IllegalArgumentException("Unknown cargo type");
    };
  }

  static void close() {
    if (instance == null) {
      throw new IllegalStateException("DockManager is not initialized");
    }
    for (Dock dock : docks) {
      dock.close();
    }
  }
}
