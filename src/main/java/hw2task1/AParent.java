package hw2task1;

public class AParent {

  private String valueParent;

  @Setter
  public void setValueParent(String str) {
    valueParent = str;
  }

  @Cache
  public String parentMethod() {
    System.out.println("original method");
    return valueParent;
  }

  @Cache
  public String overwrittenMethod(String str) {
    System.out.println("original method");
    return str;
  }
}
