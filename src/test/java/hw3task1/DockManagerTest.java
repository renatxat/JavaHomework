package hw3task1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DockManagerTest {

  private static DockManager dockManager;

  @BeforeAll
  static void setUp() {
    dockManager = DockManager.getInstance();
  }

  @AfterAll
  static void tearDown() {
    DockManager.close(); // Закрываем все доки после тестов
  }

  @Test
  void testSingletonInstance() {
    DockManager anotherInstance = DockManager.getInstance();
    assertSame(dockManager, anotherInstance, "DockManager should be a singleton");
  }

  @Test
  void testGetDockForBread() {
    Dock dock = dockManager.getDock(CargoType.BREAD);
    assertNotNull(dock, "Dock for BREAD should not be null");
    assertEquals(CargoType.BREAD, dock.getCargoType(), "Dock should be of type BREAD");
  }

  @Test
  void testGetDockForBanana() {
    Dock dock = dockManager.getDock(CargoType.BANANA);
    assertNotNull(dock, "Dock for BANANA should not be null");
    assertEquals(CargoType.BANANA, dock.getCargoType(), "Dock should be of type BANANA");
  }

  @Test
  void testGetDockForClothing() {
    Dock dock = dockManager.getDock(CargoType.CLOTHING);
    assertNotNull(dock, "Dock for CLOTHING should not be null");
    assertEquals(CargoType.CLOTHING, dock.getCargoType(), "Dock should be of type CLOTHING");
  }

  @Test
  void testGetDockForUnknownCargoType() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      dockManager.getDock(null); // Проверяем на null
    });
    assertEquals("Unknown cargo type", exception.getMessage());
  }

  @Test
  void testCloseDocks() {
    // Проверяем, что метод close() не вызывает исключений
    assertDoesNotThrow(DockManager::close);
  }

  @Test
  void testDoubleInitialization() {
    // Убедимся, что менеджер доков не может быть инициализирован дважды
    DockManager anotherInstance = DockManager.getInstance();
    assertSame(dockManager, anotherInstance);
  }
}
