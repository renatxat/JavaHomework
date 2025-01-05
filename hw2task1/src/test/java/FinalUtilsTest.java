import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinalUtilsTest {

  private AInterface cachedA;
  private final String currStr = "Hello";
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private PrintStream originalOut;

  @BeforeEach
  public void setUp() throws Exception {
    originalOut = System.out;
    System.setOut(new PrintStream(outputStreamCaptor));

    AFinal a = new AFinal(currStr);
    cachedA = (AInterface) Utils.cache(a);
  }

  @RepeatedTest(3)
  public void testMainMethodCaching() {
    cachedA.setValue(currStr);
    assertEquals(currStr, cachedA.mainMethod());
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    assertEquals(currStr, cachedA.mainMethod());
    assertEquals("", outputStreamCaptor.toString().trim());
  }

  @RepeatedTest(3)
  public void testDefaultMethodNotCaching() {
    cachedA.setValue(currStr);
    cachedA.defaultMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
    outputStreamCaptor.reset();

    cachedA.defaultMethod();
    assertEquals("original method", outputStreamCaptor.toString().trim());
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
  }
}
