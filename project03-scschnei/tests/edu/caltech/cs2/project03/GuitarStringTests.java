package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.function.Function;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuitarStringTests {
  private static String STRING_SOURCE = "src/edu/caltech/cs2/project03/CircularArrayFixedSizeQueueGuitarString.java";

  @Order(0)
  @Tag("A")
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java.util.(?!Random|function.Function)", "java.lang.reflect", "java.io");
    Inspection.assertNoImportsOf(STRING_SOURCE, regexps);
    Inspection.assertNoUsageOf(STRING_SOURCE, regexps);
  }

  @Order(0)
  @Tag("A")
  @DisplayName("Uses this(...) notation in all but one constructor")
  @Test
  public void testForThisConstructors() {
    Inspection.assertConstructorHygiene(STRING_SOURCE);
  }

  @Order(0)
  @Tag("A")
  @DisplayName("There are three static fields: the two double constants and a random value generator")
  @Test
  public void testStaticFields() {
    Reflection.assertFieldsEqualTo(CircularArrayFixedSizeQueueGuitarString.class, "static", 3);
    Stream<Field> fields = Reflection.getFields(CircularArrayFixedSizeQueueGuitarString.class);
    fields.filter(Reflection.hasModifier("static")).forEach((field) -> {
      Reflection.checkFieldModifiers(field, List.of("private", "static"));
      assertTrue(Reflection.hasModifier("final").test(field) || field.getType().equals(Random.class), "non-final static class must be a random value generator");
    });
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The overall number of fields is small")
  @Test
  public void testSmallNumberOfFields() {
    Reflection.assertFieldsLessThan(CircularArrayFixedSizeQueueGuitarString.class, "private", 5);
  }

  @Order(0)
  @Tag("A")
  @DisplayName("There are no public fields")
  @Test
  public void testNoPublicFields() {
    Reflection.assertNoPublicFields(CircularArrayFixedSizeQueueGuitarString.class);
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The public interface is correct")
  @Test
  public void testPublicInterface() {
    Reflection.assertPublicInterface(CircularArrayFixedSizeQueueGuitarString.class, List.of(
            "length",
            "pluck",
            "tic",
            "sample"
    ));
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The constructor correctly sets up the queue")
  @ParameterizedTest(name = "Test constructor with CircularArrayFixedSizeQueue and a frequency of {0} Hz; expected queue size is {1}")
  @CsvSource({
          "110, 401",
          "340, 130",
          "512, 87",
          "600.5, 74",
          "880, 51"
  })
  public void testConstructor(double frequency, int expectedSize) {
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueueGuitarString.class, double.class);
    CircularArrayFixedSizeQueueGuitarString string = Reflection.newInstance(c, frequency);
    String queueName = Reflection.getFieldByType(CircularArrayFixedSizeQueueGuitarString.class, IFixedSizeQueue.class).getName();
    IFixedSizeQueue<Double> queue = Reflection.getFieldValue(CircularArrayFixedSizeQueueGuitarString.class, queueName, string);
    assertEquals(expectedSize, queue.size(), "Queue size is not equal to expected size");
    for (double val : queue) {
      assertEquals(0, val, "All values in queue should be equal to 0");
    }
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The pluck() method randomizes the values in the queue")
  @ParameterizedTest(name = "Test pluck() with CircularArrayFixedSizeQueue and a frequency of {0} Hz")
  @CsvSource({
          "100",
          "50",
          "10",
          "8",
          "5"
  })
  public void testPluck(double frequency) {
    final double DELTA = 0.05;
    // Set up class and retrieve queue
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueueGuitarString.class, double.class);
    CircularArrayFixedSizeQueueGuitarString string = Reflection.newInstance(c, frequency);
    String queueName = Reflection.getFieldByType(CircularArrayFixedSizeQueueGuitarString.class, IFixedSizeQueue.class).getName();
    IFixedSizeQueue<Double> queue = Reflection.getFieldValue(CircularArrayFixedSizeQueueGuitarString.class, queueName, string);
    // Checks all values are initially 0
    for(double val : queue){
      assertEquals(0, val, "initial values must be 0");
    }
    string.pluck();
    double sum = 0;
    double absSum = 0;
    for(double val : queue){
      sum += val;
      absSum += abs(val);
    }
    assertEquals(0, sum/queue.size(), DELTA, "average value of uniform distribution should be near 0");
    assertEquals(0.25, absSum/queue.size(), DELTA, "average magnitude of uniform distribution should be near 0.25");
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The tic() method correctly applies the Karplus-Strong algorithm")
  @ParameterizedTest(name = "Test tic() with CircularArrayFixedSizeQueue and a frequency of {0} Hz; data file {1}.txt")
  @CsvSource({
          "10000, ticStates1",
          "8000, ticStates2",
          "5000, ticStates3"
  })
  public void testTic(double frequency, String filename) {
    // Set up scanner
    String filepath = "tests/data/" + filename + ".txt";
    Scanner in;
    try {
      in = new Scanner(new File(filepath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(filepath + " is not a valid trace file.");
    }
    // Set up class and retrieve queue
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueueGuitarString.class, double.class);
    CircularArrayFixedSizeQueueGuitarString string = Reflection.newInstance(c, frequency);
    String queueName = Reflection.getFieldByType(CircularArrayFixedSizeQueueGuitarString.class, IFixedSizeQueue.class).getName();
    IFixedSizeQueue<Double> queue = Reflection.getFieldValue(CircularArrayFixedSizeQueueGuitarString.class, queueName, string);
    // Reinitialize queue with new data
    for(int i = 0; i < queue.size(); i++){
      queue.dequeue();
      queue.enqueue(in.nextDouble());
    }
    int initSize = queue.size();
    // Pass through the same number of tics as elements in the array
    for(int i = 0; i < initSize; i++){
      string.tic();
      assertEquals(initSize, queue.size(), "queue size must remain the same");
    }
    // Compare peek() values with the expected values in the files
    while (in.hasNext()) {
      string.tic();
      assertEquals(initSize, (double)queue.size(), "queue size must remain the same");
      assertEquals(in.nextDouble(), (double)queue.peek(), "next expected value not at front of queue");
    }
  }

  @Order(0)
  @Tag("A")
  @DisplayName("The length() method correctly gives the length of the queue")
  @ParameterizedTest(name = "Test length() with CircularArrayFixedSizeQueue and a frequency of {0} Hz; expected length = {1}; iterations = {2}")
  @CsvSource({
          "110, 401, 1000",
          "340, 130, 500",
          "512, 87, 200",
          "600.5, 74, 150",
          "880, 51, 100"
  })
  public void testLength(double frequency, int expectedLength, int iterations) {
    // Set up class and retrieve queue
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueueGuitarString.class, double.class);
    CircularArrayFixedSizeQueueGuitarString string = Reflection.newInstance(c, frequency);
    String queueName = Reflection.getFieldByType(CircularArrayFixedSizeQueueGuitarString.class, IFixedSizeQueue.class).getName();
    IFixedSizeQueue<Double> queue = Reflection.getFieldValue(CircularArrayFixedSizeQueueGuitarString.class, queueName, string);

    // Pluck and make sure length doesn't change
    int initSize = queue.size();
    assertEquals(expectedLength, string.length(), "Length should be same as expected");
    assertEquals(queue.size(), string.length(), "Length should be same as queue size");
    string.pluck();
    assertEquals(initSize, string.length(), "Length should not have changed from beginning");
    assertEquals(queue.size(), string.length(), "Length should be same as queue size");

    // Run through many iterations, making sure both the queue size and length are constant
    for (int i = 0; i < iterations; i++) {
      string.tic();
      assertEquals(initSize, string.length(), "Length should not have changed from beginning");
      assertEquals(queue.size(), string.length(), "Length should be same as queue size");
    }

  }

  @Order(0)
  @Tag("A")
  @DisplayName("The sample() method gives the same values as peek()ing the queue")
  @ParameterizedTest(name = "Test sample() with CircularArrayFixedSizeQueue and a frequency of {0} Hz")
  @CsvSource({
          "110, 1000",
          "340, 500",
          "512, 200",
          "600.5, 150",
          "880, 100"
  })
  public void testSample(double frequency, int iterations) {
    // Set up class and retrieve queue
    Constructor c = Reflection.getConstructor(CircularArrayFixedSizeQueueGuitarString.class, double.class);
    CircularArrayFixedSizeQueueGuitarString string = Reflection.newInstance(c, frequency);
    String queueName = Reflection.getFieldByType(CircularArrayFixedSizeQueueGuitarString.class, IFixedSizeQueue.class).getName();
    IFixedSizeQueue<Double> queue = Reflection.getFieldValue(CircularArrayFixedSizeQueueGuitarString.class, queueName, string);

    // Pluck and make sure initial samples are correct
    assertEquals(0, string.sample(), "Sample should return 0 before plucking");
    assertEquals((double)queue.peek(), string.sample(), "Sample should same as peek()ing queue");
    string.pluck();
    assertEquals((double)queue.peek(), string.sample(), "Sample should same as peek()ing queue");

    // Run through many iterations, making sure sample() matches peek()
    for (int i = 0; i < iterations; i++) {
      string.tic();
      assertEquals((double)queue.peek(), string.sample(), "Sample should same as peek()ing queue");
    }

  }

}
