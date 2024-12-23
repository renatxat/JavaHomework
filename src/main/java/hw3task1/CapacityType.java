package hw3task1;

import java.util.concurrent.ThreadLocalRandom;

public enum CapacityType {
  SMALL(10), MEDIUM(20), LARGE(50);
  private final int capacity;

  CapacityType(int capacity) {
    this.capacity = capacity;
  }

  public int get() {
    return capacity;
  }

  public static CapacityType getRandomCapacityType() {
    return CapacityType.values()[ThreadLocalRandom.current()
        .nextInt(CapacityType.values().length)];
  }
}
