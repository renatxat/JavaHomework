package hw3task1;

import java.util.Random;

public class Main {

  public static void main(String[] args) {
    Tunnel tunnel = new Tunnel();
    for (int i = 0; i < 100; ++i) {
      tunnel.addShip();
      try {
        Thread.sleep(new Random().nextInt(1000));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    tunnel.close();
    DockManager.close();
  }
}
