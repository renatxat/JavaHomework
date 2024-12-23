package hw3task1;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TunnelTest {

  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;
  private Tunnel tunnel;
  private final double EPS = 100;

  @BeforeEach
  void CustomInput() {
    tunnel = new Tunnel();
    // Сохраняем старый стандартный вывод
    this.originalOut = System.out;
    // Создаем поток для захвата вывода
    this.outputStream = new ByteArrayOutputStream();
    PrintStream newOut = new PrintStream(outputStream);
    // Перенаправляем стандартный вывод
    System.setOut(newOut);
  }

  @Test
  void AddShip() {
    tunnel.addShip();
    assertNotNull(tunnel);
  }

  @Test
  void AddFewShips() {
    outputStream.reset();
    double time = System.currentTimeMillis();
    int numberOfLines = 0; // Подсчитываем количество строк
    for (int i = 0; i < 5; ++i) {
      tunnel.addShip();
    }
    while (numberOfLines < 5) {
      String output = outputStream.toString();
      numberOfLines = output.split(" has exited the tunnel", -1).length - 1;
      // Подсчитываем количество созданных кораблей
    }
    assertTrue(abs(System.currentTimeMillis() - time - Tunnel.TRAVEL_TIME) < EPS);
  }

  @Test
  void AddManyShips() {
    outputStream.reset();
    final double time = System.currentTimeMillis();
    for (int i = 0; i < 7; ++i) {
      tunnel.addShip();
    }

    int numberOfLines = 0; // Подсчитываем количество строк
    while (numberOfLines < 7) {
      String output = outputStream.toString();
      numberOfLines = output.split(" has exited the tunnel", -1).length - 1;
      // Подсчитываем количество созданных кораблей
    }
    assertTrue(abs(System.currentTimeMillis() - time - 2 * Tunnel.TRAVEL_TIME) < EPS);
  }

  @Test
  void close() {
    tunnel.close();
  }

  @AfterEach
  public void tearDown() {
    System.setOut(this.originalOut);
  }

}