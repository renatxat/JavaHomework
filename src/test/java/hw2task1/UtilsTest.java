package hw2task1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

  private A cachedA;
  private String currStr = "Hello";
  private int crutchNum = 0;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private PrintStream originalOut;

  @BeforeEach
  public void setUp() throws Exception {
    originalOut = System.out;
    System.setOut(new PrintStream(outputStreamCaptor));

    A a = new A();
    cachedA = (A) Utils.cache(a);
  }

  @RepeatedTest(3)
  public void testMainMethodCaching() {
    cachedA.setValue(currStr);
    assertEquals(currStr, cachedA.mainMethod());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals(currStr, cachedA.mainMethod());
    assertEquals("", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testNewMethodCaching() {
    cachedA.setValue(currStr);
    assertEquals("new", cachedA.newMethod());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals("new", cachedA.newMethod());
    assertEquals("", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testParentMethodCaching() {
    currStr = "World";
    cachedA.setValueParent(currStr);
    assertEquals(currStr, cachedA.parentMethod());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals(currStr, cachedA.parentMethod());
    assertEquals("", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testDefaultMethodNotCaching() {
    cachedA.setValue(currStr);
    cachedA.defaultMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    cachedA.defaultMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testVoidMethodCaching() {
    cachedA.setValue(currStr);
    cachedA.voidMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    cachedA.voidMethod();
    assertEquals("", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testMethodWithoutCacheNotCaching() {
    cachedA.setValue(currStr);
    assertEquals(2024, cachedA.methodWithoutCache());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals(2024, cachedA.methodWithoutCache());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testOverwrittenMethodNotCaching() {
    cachedA.setValue(currStr);
    String tempStr = "ABCD";
    assertEquals(tempStr, cachedA.overwrittenMethod(tempStr));
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals(tempStr, cachedA.overwrittenMethod(tempStr));
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @RepeatedTest(3)
  public void testSetValueParent() {
    crutchNum = 1 - crutchNum;
    if (crutchNum % 2 == 0) {
      cachedA.setValue(currStr);
    } else {
      cachedA.setValueParent(currStr);
    }

    cachedA.mainMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    cachedA.defaultMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    cachedA.parentMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
  }
}
