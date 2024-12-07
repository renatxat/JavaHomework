package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortTest {

  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;
  private Port generator;

  @BeforeEach
  void CustomInput() {
    generator = new Port();
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
    generator.AddShip();
    assertNotNull(generator);
  }

  @Test
  void AddFewShips() {
    outputStream.reset();
    double time = System.currentTimeMillis();
    int numberOfLines = 0; // Подсчитываем количество строк
    for (int i = 0; i < 5; ++i) {
      generator.AddShip();
    }
    while (numberOfLines < 5) {
      String output = outputStream.toString();
      numberOfLines = output.split(" has exited the tunnel", -1).length - 1;
      // Подсчитываем количество созданных кораблей
    }
    assertTrue(System.currentTimeMillis() - time < 1050);
  }

  @Test
  void AddManyShips() {
    outputStream.reset();
    final double time = System.currentTimeMillis();
    int numberOfLines = 0; // Подсчитываем количество строк
    for (int i = 0; i < 7; ++i) {
      generator.AddShip();
    }
    while (numberOfLines < 7) {
      String output = outputStream.toString();
      numberOfLines = output.split(" has exited the tunnel", -1).length - 1;
      // Подсчитываем количество созданных кораблей
    }
    assertTrue(System.currentTimeMillis() - time >= 2000);
  }

  @Test
  void close() {
    generator.close();
  }

  @AfterEach
  public void tearDown() {
    System.setOut(this.originalOut);
  }

}