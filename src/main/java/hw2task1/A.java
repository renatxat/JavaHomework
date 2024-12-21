package hw2task1;

public class A extends AParent implements AInterface {

  private String value;

  public A(String str) {
    value = str;
  }

  public A() {
    value = "";
  }

  @Cache
  @Override
  public String mainMethod() {
    System.out.println("original method");
    return value;
  }

  @Override
  public String overwrittenMethod(String str) {
    return super.overwrittenMethod(str);
  }

  @Override
  @Setter
  public void setValue(String value) {
    this.value = value;
  }

  @Cache
  public String newMethod() {
    System.out.println("original method");
    return "new";
  }

  @Cache
  public void voidMethod() {
    System.out.println("original method");
  }

  public int methodWithoutCache() {
    System.out.println("original method");
    return 2024;
  }
}
