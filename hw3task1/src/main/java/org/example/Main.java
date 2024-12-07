package org.example;

import java.util.Random;

public class Main {

  public static void main(String[] args) {
    Port generator = new Port();
    for (int i = 0; i < 100; ++i) {
      generator.AddShip();
      try {
        Thread.sleep(new Random().nextInt(1000));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    generator.close();
    DockManager.close();
  }
}
