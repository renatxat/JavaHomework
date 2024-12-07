package org.example;

public enum CapacityType {
  SMALL(10), MEDIUM(20), LARGE(50);
  private final int capacity;

  CapacityType(int capacity) {
    this.capacity = capacity;
  }

  public int get() {
    return capacity;
  }
}
