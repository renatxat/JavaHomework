package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DockManagerTest {

  @Test
  void testWithEmpty() {
    Exception thrown = assertThrows(Exception.class, () -> {
      DockManager.close();
    });
    assertInstanceOf(IllegalStateException.class, thrown);
  }

  @Test
  void getInstance() {
    DockManager dockManager = DockManager.getInstance();
    assertNotNull(dockManager);
  }

  @Test
  void getDock() {
    DockManager dockManager = DockManager.getInstance();
    assertNotNull(dockManager);
    Dock dock = dockManager.getDock(CargoType.BREAD);
    assertNotNull(dock);
  }

  @Test
  void close() {
    DockManager dockManager = DockManager.getInstance();
    dockManager.close();
  }
}
