package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IStack;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArrayDequeTests {
  private static String ARRAY_DEQUE_SOURCE ="src/edu/caltech/cs2/datastructures/ArrayDeque.java";

  // ARRAYDEQUE-SPECIFIC TESTS ----------------------------------------

  @Order(0)
  @Tag("C")
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java.util.(?!Iterator)", "java.lang.reflect", "java.io");
    Inspection.assertNoImportsOf(ARRAY_DEQUE_SOURCE, regexps);
    Inspection.assertNoUsageOf(ARRAY_DEQUE_SOURCE, regexps);
  }

  @Order(0)
  @Tag("C")
  @DisplayName("There is an integer default capacity static field and an integer default grow factor static field")
  @Test
  public void testConstantFields() {
    Reflection.assertFieldsEqualTo(ArrayDeque.class, "static", 2);
    Stream<Field> fields = Reflection.getFields(ArrayDeque.class);
    fields.filter(Reflection.hasModifier("static")).forEach((field) -> {
      Reflection.checkFieldModifiers(field, List.of("private", "static", "final"));
      assertEquals(field.getType(), int.class, "static fields must be of type int");
    });
  }

  @Order(0)
  @Tag("C")
  @DisplayName("The overall number of fields is small")
  @Test
  public void testSmallNumberOfFields() {
    Reflection.assertFieldsLessThan(ArrayDeque.class, "private", 5);
  }

  @Order(0)
  @Tag("C")
  @DisplayName("There are no public fields")
  @Test
  public void testNoPublicFields() {
    Reflection.assertNoPublicFields(ArrayDeque.class);
  }

  @Order(0)
  @Tag("C")
  @DisplayName("The public interface is correct")
  @Test
  public void testPublicInterface() {
    Reflection.assertPublicInterface(ArrayDeque.class, List.of(
            "addFront",
            "addBack",
            "removeFront",
            "removeBack",
            "enqueue",
            "dequeue",
            "push",
            "pop",
            "peek",
            "peekFront",
            "peekBack",
            "iterator",
            "size",
            "toString"
    ));
  }

  // Test commented out because it fails due to iterator constructor also being in class
  /*
  @Order(0)
  @Tag("C")
  @DisplayName("Uses this(...) notation in all but one constructor")
  @Test
  public void testForThisConstructors() {
    Inspection.assertConstructorHygiene(ARRAY_DEQUE_SOURCE);
  }
  */


  // TOSTRING TESTS ---------------------------------------------------

  @Order(0)
  @Tag("C")
  @DisplayName("toString is correctly overridden")
  @Test
  public void testToStringOverride() {
    Reflection.assertMethodCorrectlyOverridden(ArrayDeque.class, "toString");
  }

  @Order(0)
  @Tag("C")
  @DisplayName("toString() matches java.util.ArrayDeque")
  @ParameterizedTest(name = "Test toString() on [{arguments}]")
  @ValueSource(strings = {
          "0, 1, 2, 3", "5, 4, 3, 2, 1", "8, 3, 5, 7, 4, 3, 12, 12, 1"
  })
  public void testToString(String inputs) {
    java.util.ArrayDeque<String> reference = new java.util.ArrayDeque<String>();
    edu.caltech.cs2.datastructures.ArrayDeque<String> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    for (String value : inputs.trim().split(", ")) {
      assertEquals(reference.toString(), me.toString(), "toString outputs should be the same");
      reference.addLast(value);
      me.addBack(value);
    }
  }


  // ICOLLECTION TESTS ---------------------------------------------------

  @Order(1)
  @Tag("C")
  @DisplayName("Testing the basics of various ICollection functions")
  @ParameterizedTest(name = "Test add(), size(), isEmpty(), contains(), and clear() on [{arguments}]")
  @ValueSource(strings = {
          "0, 1, 2, 3", "5, 4, 3, 2, 1", "8, 3, 5, 7, 4, 3, 12, 12, 1"
  })
  public void testCollectionFunctions(String inputs) {
    ICollection<String> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    List<String> reference = new java.util.ArrayList<>();
    // Check that collection is empty
    assertEquals(true, me.isEmpty(), "collection should be empty");

    // Check that values are not in collection
    for (String value : inputs.trim().split(", ")) {
      assertEquals(false, me.contains(value), "value should not be contained");
    }
    // Add values to collection
    for (String value : inputs.trim().split(", ")) {
      me.add(value);
      reference.add(value);
    }
    // Check that size() and isEmpty() is correct
    assertEquals(reference.size(), me.size(), "sizes should be equal");
    assertEquals(false, me.isEmpty(), "collection should not be empty");
    // Check that values are in collection
    for (String value : inputs.trim().split(", ")) {
      assertEquals(true, me.contains(value), "value should be contained");
    }
    // Clear and make sure size() and isEmpty() match
    me.clear();
    assertEquals(0, me.size(), "size should be 0");
    assertEquals(true, me.isEmpty(), "collection should be empty");
    // Check that values are not in collection
    for (String value : inputs.trim().split(", ")) {
      assertEquals(false, me.contains(value), "value should not be contained");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for add(...)")
  @ParameterizedTest(name = "Test add()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "100, 3000", "42, 1000"
  })
  public void stressTestAdd(int seed, int size) {
    Random r = new Random(seed);
    List<Integer> reference = new java.util.ArrayList<>();
    ICollection<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test adding values updates size and displays contained correctly
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.add(num);
      me.add(num);
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.contains(num), me.contains(num), "value should be contained");
    }
    // Test that values not in collection are not contained
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      assertEquals(reference.contains(num), me.contains(num), "contained values do not match");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for contains(...)")
  @ParameterizedTest(name = "Test contains() with {1} random numbers and seed = {0}")
  @CsvSource({
          "100, 3000", "42, 1000"
  })
  public void stressTestContains(int seed, int size) {
    Random r = new Random(seed);
    List<Integer> nums = new java.util.ArrayList<>();
    ICollection<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Add values to both the list of nums and test collection
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      nums.add(num);
      me.add(num);
    }
    // Shuffle order of nums and check that all are contained in the collection
    Collections.shuffle(nums);
    for (int num : nums) {
      assertEquals(true, me.contains(num), "value should be contained");
    }

    // Test that values not in collection are not contained
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      assertEquals(nums.contains(num), me.contains(num), "contained values do not match");
    }
  }


  // IDEQUE TESTS ---------------------------------------------------

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for addFront(...) and peekFront(...)")
  @ParameterizedTest(name = "Test addFront()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "100, 300", "42, 500"
  })
  public void stressTestAddFront(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first peek is null
    assertEquals(reference.peekFirst(), me.peekFront(), "empty peek should return null");
    // Test adding values updates size and displays contained correctly
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.addFirst(num);
      me.addFront(num);
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.peekFirst(), me.peekFront(), "peeks should be the same");
      assertEquals(reference.toString(), me.toString(), "toStrings()s are not equal");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for addBack(...) and peekBack(...)")
  @ParameterizedTest(name = "Test addBack()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "100, 300", "42, 500"
  })
  public void stressTestAddBack(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first peek is null
    assertEquals(reference.peekLast(), me.peekBack(), "empty peek should return null");
    // Test adding values updates size and displays contained correctly
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.addLast(num);
      me.addBack(num);
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.peekLast(), me.peekBack(), "peeks should be the same");
      assertEquals(reference.toString(), me.toString(), "toStrings()s are not equal");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for removeFront(...)")
  @ParameterizedTest(name = "Test removeFront()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "101, 300", "45, 500"
  })
  public void stressTestRemoveFront(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<Integer>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first removeFront is null
    assertEquals(null, me.removeFront(), "empty removeFront should return null");
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.addFirst(num);
      me.addFront(num);
      assertEquals(reference.peekFirst(), me.peekFront(),"return values of peekFront()s are not equal");
      if (r.nextBoolean()) {
        assertEquals(reference.removeFirst(), me.removeFront(),"return values of removeFront()s are not equal");
        assertEquals(reference.peekFirst(), me.peekFront(),"return values of peekFront()s are not equal");
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.toString(), me.toString(), "toStrings()s are not equal");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for removeBack(...)")
  @ParameterizedTest(name = "Test removeBack()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "101, 300", "45, 500"
  })
  public void stressTestRemoveBack(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<Integer>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first removeBack is null
    assertEquals(null, me.removeBack(), "empty removeBack should return null");
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.addLast(num);
      me.addBack(num);
      assertEquals(reference.peekLast(), me.peekBack(),"return values of peekBack()s are not equal");
      if (r.nextBoolean()) {
        assertEquals(reference.removeLast(), me.removeBack(),"return values of removeBack()s are not equal");
        assertEquals(reference.peekLast(), me.peekBack(),"return values of peekBack()s are not equal");
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.toString(), me.toString(), "toStrings()s are not equal");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test full IDeque")
  @ParameterizedTest(name = "Test all IDeque methods {1} random numbers with seed = {0}")
  @CsvSource({
          "102, 300", "52, 500"
  })
  public void stressTestFullDeque(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<Integer>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      // Add to either front or back
      if (r.nextBoolean()) {
        reference.addFirst(num);
        me.addFront(num);
      }
      else {
        reference.addLast(num);
        me.addBack(num);
      }
      // Test that peeks are correct
      assertEquals(reference.peekFirst(), me.peekFront(),"return values of peekFront()s are not equal");
      assertEquals(reference.peekLast(), me.peekBack(),"return values of peekBacks()s are not equal");
      // If true, remove an element
      if (r.nextBoolean()) {
        // If true, remove from front, else remove from back
        if (r.nextBoolean()) {
          assertEquals(reference.removeFirst(), me.removeFront(),"return values of removeFront()s are not equal");
        }
        else {
          assertEquals(reference.removeLast(), me.removeBack(),"return values of removeBack()s are not equal");
        }
        assertEquals(reference.peekFirst(), me.peekFront(),"return values of peekFront()s are not equal");
        assertEquals(reference.peekLast(), me.peekBack(),"return values of peekBacks()s are not equal");
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.toString(), me.toString(), "toStrings()s are not equal");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Test for addAll(...)")
  @ParameterizedTest(name = "Test addAll with {1} random numbers and seed = {0}")
  @CsvSource({
          "99, 300", "48, 500"
  })
  public void testAddAll(int seed, int size) {
    Random r = new Random(seed);
    ICollection<Integer> coll = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    IDeque<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      coll.add(num);
    }
    me.addAll(coll);
    for (int num : coll) {
      assertEquals(true, me.contains(num), "value should be contained in Deque");
    }
  }


  // ISTACK TESTS ----------------------------------------------------------

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for push(...) and peek(...)")
  @ParameterizedTest(name = "Test push()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "99, 3000", "40, 5000"
  })
  public void stressTestPush(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<>();
    IStack<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first peek is null
    assertEquals(reference.peek(), me.peek(), "empty peek should return null");
    // Test adding values updates size and peek correctly
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.push(num);
      me.push(num);
      assertEquals(reference.size(), me.size(), "size()s are not equal");
      assertEquals(reference.peek(), me.peek(), "peeks should be the same");
    }
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for pop(...)")
  @ParameterizedTest(name = "Test pop()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "98, 3000", "39, 5000"
  })
  public void stressTestPop(int seed, int size) {
    Random r = new Random(seed);
    Deque<Integer> reference = new java.util.ArrayDeque<>();
    IStack<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
    // Test that first pop is null
    assertEquals(null, me.pop(), "empty pop should return null");
    for (int i = 0; i < size; i++) {
      int num = r.nextInt();
      reference.push(num);
      me.push(num);
      assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
      if (r.nextBoolean()) {
        assertEquals(reference.pop(), me.pop(),"return values of pop()s are not equal");
        assertEquals(reference.peek(), me.peek(),"return values of peek()s are not equal");
      }
      assertEquals(reference.size(), me.size(), "size()s are not equal");
    }
  }


  // IQUEUE TESTS ----------------------------------------------------------

  @Order(1)
  @Tag("C")
  @DisplayName("Stress test for enqueue(...) and peek(...)")
  @ParameterizedTest(name = "Test enqueue()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "97, 3000", "38, 5000"
  })
  public void stressTestEnqueue(int seed, int size) {
    Random r = new Random(seed);
    Queue<Integer> reference = new java.util.ArrayDeque<>();
    IQueue<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
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
  @Tag("C")
  @DisplayName("Stress test for dequeue(...)")
  @ParameterizedTest(name = "Test dequeue()ing {1} random numbers with seed = {0}")
  @CsvSource({
          "98, 3000", "39, 5000"
  })
  public void stressTestDequeue(int seed, int size) {
    Random r = new Random(seed);
    Queue<Integer> reference = new java.util.ArrayDeque<>();
    IQueue<Integer> me = new edu.caltech.cs2.datastructures.ArrayDeque<>();
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


  // TIME COMPLEXITY TESTS ------------------------------------------------

  @Order(1)
  @Tag("C")
  @DisplayName("addFront() and removeFront() take linear time")
  @Test()
  public void testFrontDequeOperationComplexity() {
    Function<Integer, IDeque<Integer>> provide = (Integer numElements) -> {
      IDeque<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.addFront(i);
      }
      return q;
    };
    Consumer<IDeque<Integer>> addFront = (IDeque<Integer> q) -> q.addFront(0);
    Consumer<IDeque<Integer>> removeFront = (IDeque<Integer> q) -> q.removeFront();

    RuntimeInstrumentation.assertAtMost("addFront", RuntimeInstrumentation.ComplexityType.LINEAR, provide, addFront, 8);
    RuntimeInstrumentation.assertAtMost("removeFront", RuntimeInstrumentation.ComplexityType.LINEAR, provide, removeFront, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("addBack() and removeBack() take linear time")
  @Test()
  public void testBackDequeOperationComplexity() {
    Function<Integer, IDeque<Integer>> provide = (Integer numElements) -> {
      IDeque<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.addBack(i);
      }
      return q;
    };
    Consumer<IDeque<Integer>> addBack = (IDeque<Integer> q) -> q.addBack(0);
    Consumer<IDeque<Integer>> removeBack = (IDeque<Integer> q) -> q.removeBack();

    RuntimeInstrumentation.assertAtMost("addBack", RuntimeInstrumentation.ComplexityType.LINEAR, provide, addBack, 8);
    RuntimeInstrumentation.assertAtMost("removeBack", RuntimeInstrumentation.ComplexityType.LINEAR, provide, removeBack, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("enqueue() and dequeue() take linear time")
  @Test()
  public void testQueueOperationComplexity() {
    Function<Integer, IQueue<Integer>> provide = (Integer numElements) -> {
      IQueue<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.enqueue(i);
      }
      return q;
    };
    Consumer<IQueue<Integer>> enqueue = (IQueue<Integer> q) -> q.enqueue(0);
    Consumer<IQueue<Integer>> dequeue = (IQueue<Integer> q) -> q.dequeue();

    RuntimeInstrumentation.assertAtMost("enqueue", RuntimeInstrumentation.ComplexityType.LINEAR, provide, enqueue, 8);
    RuntimeInstrumentation.assertAtMost("dequeue", RuntimeInstrumentation.ComplexityType.LINEAR, provide, dequeue, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("push() and pop() take constant time")
  @Test()
  public void testStackOperationComplexity() {
    Function<Integer, IStack<Integer>> provide = (Integer numElements) -> {
      IStack<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.push(i);
      }
      return q;
    };
    Consumer<IStack<Integer>> push = (IStack<Integer> q) -> q.push(0);
    Consumer<IStack<Integer>> pop = (IStack<Integer> q) -> q.pop();

    RuntimeInstrumentation.assertAtMost("push", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, push, 8);
    RuntimeInstrumentation.assertAtMost("pop", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, pop, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("peek() takes constant time")
  @Test()
  public void testPeekComplexity() {
    Function<Integer, IStack<Integer>> provide = (Integer numElements) -> {
      IStack<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.push(i);
      }
      return q;
    };
    Consumer<IStack<Integer>> peek = (IStack<Integer> q) -> q.peek();

    RuntimeInstrumentation.assertAtMost("peek", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, peek, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("peekFront() takes constant time")
  @Test()
  public void testPeekFrontComplexity() {
    Function<Integer, IDeque<Integer>> provide = (Integer numElements) -> {
      IDeque<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.addFront(i);
      }
      return q;
    };
    Consumer<IDeque<Integer>> peekFront = (IDeque<Integer> q) -> q.peekFront();

    RuntimeInstrumentation.assertAtMost("peekFront", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, peekFront, 8);
  }

  @Order(1)
  @Tag("C")
  @DisplayName("peekBack() takes constant time")
  @Test()
  public void testPeekBackComplexity() {
    Function<Integer, IDeque<Integer>> provide = (Integer numElements) -> {
      IDeque<Integer> q = new ArrayDeque<>();
      for (int i = 0; i < numElements; i++) {
        q.addBack(i);
      }
      return q;
    };
    Consumer<IDeque<Integer>> peekBack = (IDeque<Integer> q) -> q.peekBack();

    RuntimeInstrumentation.assertAtMost("peekBack", RuntimeInstrumentation.ComplexityType.CONSTANT, provide, peekBack, 8);
  }
}