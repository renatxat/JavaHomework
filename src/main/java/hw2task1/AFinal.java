package hw2task1;

public final class AFinal extends AParent implements AInterface {

  private String value;

  public AFinal(String str) {
    value = str;
  }

  @Cache
  @Override
  public String mainMethod() {
    System.out.println("original method");
    return value;
  }

  @Override
  @Setter
  public void setValue(String value) {
    this.value = value;
  }
}
