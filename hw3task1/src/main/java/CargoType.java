import java.util.concurrent.ThreadLocalRandom;

public enum CargoType {
  BREAD, BANANA, CLOTHING;

  public static CargoType getRandomCargoType() {
    return CargoType.values()[ThreadLocalRandom.current()
        .nextInt(CargoType.values().length)];
  }
}
