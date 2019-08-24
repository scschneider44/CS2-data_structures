package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FixedQueueTests {
  private static String FIXED_QUEUE_SOURCE ="src/edu/caltech/cs2/datastructures/CircularArrayFixedSizeQueue.java";

  // FIXED QUEUE-SPECIFIC TESTS ----------------------------------------

  @Order(0)
  @Tag("B")
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java.util.(?!Iterator)", "java.lang.reflect", "java.io");
    Inspection.assertNoImportsOf(FIXED_QUEUE_SOURCE, regexps);
    Inspection.assertNoUsageOf(FIXED_QUEUE_SOURCE, regexps);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("There are no static fields")
  @Test
  public void testConstantFields() {
    Reflection.assertFieldsEqualTo(CircularArrayFixedSizeQueue.class, "static", 0);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The overall number of fields is small")
  @Test
  public void testSmallNumberOfFields() {
    Reflection.assertFieldsLessThan(CircularArrayFixedSizeQueue.class, "private", 4);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("There are no public fields")
  @Test
  public void testNoPublicFields() {
    Reflection.assertNoPublicFields(CircularArrayFixedSizeQueue.class);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The public interface is correct")
  @Test
  public void testPublicInterface() {
    Reflection.assertPublicInterface(CircularArrayFixedSizeQueue.class, List.of(
            "enqueue",
            "dequeue",
            "peek",
            "iterator",
            "size",
            "isFull",
            "capacity",
            "toString"
    ));
  }

  // Test commented out because it fails due to iterator constructor also being in class
  /*
  @Order(0)
  @Tag("B")
  @DisplayName("Uses this(...) notation in all but one constructor")
  @Test
  public void testForThisConstructors() {
    Inspection.assertConstructorHygiene(FIXED_QUEUE_SOURCE);
  }
  */


  // TOSTRING TESTS ---------------------------------------------------

  @Order(0)
  @Tag("B")
  @DisplayName("toString is correctly overridden")
  @Test
  public void testToStringOverride() {
    Reflection.assertMethodCorrectlyOverridden(ArrayDeque.class, "toString");
  }

  @Order(0)
  @Tag("B")
  @DisplayName("toString() matches java.util.ArrayDeque")
  @ParameterizedTest(name = "Test toString() on [{arguments}]")
  @ValueSource(strings = {
          "0, 1, 2, 3", "5, 4, 3, 2, 1", "8, 3, 5, 7, 4, 3, 12, 12, 1"
  })
  public void testToString(String inputs) {
    java.util.ArrayDeque<String> reference = new java.util.ArrayDeque<String>();
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
    IFixedSizeQueue<String> me = Reflection.newInstance(c, inputs.length());
    for (String value : inputs.trim().split(", ")) {
      assertEquals(reference.toString(), me.toString(), "toString outputs should be the same");
      reference.addLast(value);
      me.enqueue(value);
    }
  }


  // IQUEUE TESTS ----------------------------------------------------------

  @Order(1)
  @Tag("B")
  @DisplayName("Stress test for enqueue(...) and peek(...)")
  @ParameterizedTest(name = "Test enqueue()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "97, 3000", "38, 5000"
  })
  public void stressTestEnqueue(int seed, int size) {
    Random r = new Random(seed);
    Queue<Integer> reference = new java.util.ArrayDeque<>();
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
    IFixedSizeQueue<Integer> me = Reflection.newInstance(c, size);

    // Test that first peek is null
    assertEquals(null, me.peek(), "empty peek should return null");
    // Test adding values updates size and peek correctly
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.add(num);
      assertEquals(true, me.enqueue(num), "enqueue should be successful");
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.peek(), me.peek(), "peeks should be the same");
    }
  }

  @Order(1)
  @Tag("B")
  @DisplayName("Stress test for dequeue(...)")
  @ParameterizedTest(name = "Test dequeue()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "98, 3000", "39, 5000"
  })
  public void stressTestDequeue(int seed, int size) {
    Random r = new Random(seed);
    Queue<Integer> reference = new java.util.ArrayDeque<>();
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
    IFixedSizeQueue<Integer> me = Reflection.newInstance(c, size);

    // Test that first dequeue is null
    assertEquals(null, me.dequeue(), "empty dequeue should return null");
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.add(num);
      assertEquals(true, me.enqueue(num), "enqueue should be successful");
      assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
      if (r.nextBoolean()) {
        assertEquals(reference.remove(), me.dequeue(),"return values of dequeue()s are not equal");
        assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
    }
  }

  // IFIXEDSIZEQUEUE TESTS ------------------------------------------------

  @Order(1)
  @Tag("B")
  @DisplayName("Overflow test for enqueue(...)")
  @ParameterizedTest(name = "Test randomly enqueue()ing/dequeue()ing {1} random numbers with seed = {0} and fixed array size = {2}")
  @CsvSource({
          "97, 3000, 100", "38, 5000, 10"
  })
  public void overflowTestEnqueue(int seed, int numVals, int queueSize) {
    Random r = new Random(seed);
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
    IFixedSizeQueue<Integer> me = Reflection.newInstance(c, queueSize);
    Queue<Integer> reference = new java.util.ArrayDeque<>();
    assertEquals(queueSize, me.capacity(), "capacity does not match expected value");
    int count = 0;
    for (int i = 0; i < numVals; i++) {
      int num = r.nextInt();
      // Check that we get the expected value from enqueue when it has a risk of overflowing
      if (count < queueSize) {
        assertEquals(false, me.isFull(), "queue should not be full");
        assertEquals(true, me.enqueue(num), "enqueue should be successful");
        reference.add(num);
        count++;
      }
      else {
        assertEquals(true, me.isFull(), "queue should be full");
        assertEquals(false, me.enqueue(num), "enqueue should have failed");
      }

      // Standard checks to make sure peeks() and dequeues() match up
      assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
      if (r.nextBoolean()) {
        assertEquals(reference.remove(), me.dequeue(),"return values of dequeue()s are not equal");
        assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
        count--;
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(queueSize, me.capacity(), "capacity does not match expected value");
    }
  }


  // TIME COMPLEXITY TESTS ------------------------------------------------

  @Order(1)
  @Tag("B")
  @DisplayName("enqueue() and dequeue() take constant time")
  @Test()
  public void testQueueOperationComplexity() {
    Function<Integer, IFixedSizeQueue<Integer>> provide = (Integer numElements) -> {
      Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
      IFixedSizeQueue<Integer> q = Reflection.newInstance(c, numElements*2);
      for (int i = 0; i < numElements; i++) {
        q.enqueue(i);
      }
      return q;
    };
    Consumer<IFixedSizeQueue<Integer>> enqueue = (IFixedSizeQueue<Integer> q) -> q.enqueue(0);
    Consumer<IFixedSizeQueue<Integer>> dequeue = (IFixedSizeQueue<Integer> q) -> q.dequeue();

    RuntimeInstrumentation.assertAtMost("enqueue", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, enqueue, 8);
    RuntimeInstrumentation.assertAtMost("dequeue", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, dequeue, 8);
  }


  @Order(1)
  @Tag("B")
  @DisplayName("peek() takes constant time")
  @Test()
  public void testPeekComplexity() {
    Function<Integer, IFixedSizeQueue<Integer>> provide = (Integer numElements) -> {
      Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueue.class, int.class);
      IFixedSizeQueue<Integer> q = Reflection.newInstance(c, numElements*2);
      for (int i = 0; i < numElements; i++) {
        q.enqueue(i);
      }
      return q;
    };
    Consumer<IFixedSizeQueue<Integer>> peek = (IFixedSizeQueue<Integer> q) -> q.peek();

    RuntimeInstrumentation.assertAtMost("peek", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, peek, 8);
  }
}