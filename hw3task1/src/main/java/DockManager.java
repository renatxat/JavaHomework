/**
 * Это менеджер причалов для погрузки, который предоставляет доступ к причалам по типу груза. Для
 * каждого типа груза создаётся ровно один причал. Логика ожидания начало погрузки реализована в
 * {@link Dock} Реализован с помощью потокобезопасный синглтона, чтобы можно было запросить нужный
 * причал из любого места в коде. При использовании метода close все причалы закрываются, состояние
 * переходит в null. Можно создать и использовать новый DockManager.
 * <p>
 * На самом деле, хватает и обычного синглтона, так как нет борьбы за ресурс.
 */
public class DockManager {

  private static volatile DockManager instance;

  private static Dock[] docks;

  private DockManager() {
    docks = new Dock[]{new Dock(CargoType.BREAD),
        new Dock(CargoType.BANANA),
        new Dock(CargoType.CLOTHING)};
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

  /**
   * @return возвращает заранее созданный причал по типу груза
   */
  public Dock getDock(CargoType cargoType) {
    if (instance == null) {
      throw new IllegalStateException("DockManager is not initialized");
    }
    if (cargoType == null) {
      throw new IllegalArgumentException("Unknown cargo type");
    }
    return switch (cargoType) {
      case BREAD -> docks[0];
      case BANANA -> docks[1];
      case CLOTHING -> docks[2];
      default -> throw new IllegalArgumentException("Unknown cargo type");
    };
  }

  public static void close() {
    if (instance == null) {
      throw new IllegalStateException("DockManager is not initialized");
    }
    instance = null;
    for (Dock dock : docks) {
      dock.close();
    }
  }
}
