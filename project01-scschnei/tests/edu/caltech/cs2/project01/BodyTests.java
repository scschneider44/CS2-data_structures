package edu.caltech.cs2.project01;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.Vector2D;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BodyTests {
  private static String BODIES_SOURCE ="src/edu/caltech/cs2/project01/Body.java";
  private static double EQUALITY_THRESHOLD = 0.0000000000001;

  @Order(0)
  @Tag("B")
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java.util", "java.lang.reflect", "java.io");
    Inspection.assertNoImportsOf(BODIES_SOURCE, regexps);
    Inspection.assertNoUsageOf(BODIES_SOURCE, regexps);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("G is the only static field, has the correct modifiers, and the correct value")
  @Test
  public void testGConstant() {
    Field G = Reflection.getFieldByModifiers(Body.class, "static");
    Reflection.checkFieldModifiers(G, List.of("private", "static", "final"));
    assertEquals(6.67E-11, Reflection.getFieldValue(Body.class, G.getName(), Body.class));
  }

  @Order(0)
  @Tag("B")
  @DisplayName("Position, velocity, and force are represented as Point2D fields")
  @Test
  public void testPoint2DFields() {
    Reflection.assertFieldsEqualTo(Body.class, "private", Vector2D.class, 3);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("Mass and filename are constant, private fields")
  @Test
  public void testConstantFields() {
    Field filenameField = Reflection.getFieldByType(Body.class, String.class);
    Reflection.checkFieldModifiers(filenameField, List.of("private", "final"));

    Field massField = Reflection.getNonStaticFieldByType(Body.class, double.class);
    Reflection.checkFieldModifiers(massField, List.of("private", "final"));
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The overall number of fields is small")
  @Test
  public void testSmallNumberOfFields() {
    Reflection.assertFieldsLessThan(Body.class, "private", 6);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("There are no public fields")
  @Test
  public void testNoPublicFields() {
    Reflection.assertNoPublicFields(Body.class);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The public interface is correct")
  @Test
  public void testPublicInterface() {
    Reflection.assertPublicInterface(Body.class, List.of(
            "calculateNewForceFrom",
            "getCurrentPosition",
            "getFileName",
            "updatePosition",
            "toString"
    ));
  }

  @Order(0)
  @Tag("B")
  @DisplayName("toString is correctly overridden")
  @Test
  public void testToStringOverride() {
    Reflection.assertMethodCorrectlyOverridden(Body.class, "toString");
  }


  @Order(0)
  @Tag("B")
  @DisplayName("Uses this(...) notation in all but one constructor")
  @Test
  public void testForThisConstructors() {
    Inspection.assertConstructorHygiene(BODIES_SOURCE);
  }

  private void testConstructorFor(Body b, double xp, double yp, double xv, double yv, double mass, String filename) {
    Field filenameField = Reflection.getFieldByType(Body.class, String.class);
    assertEquals(filename, Reflection.getFieldValue(Body.class, filenameField.getName(), b));

    Field massField = Reflection.getNonStaticFieldByType(Body.class, double.class);
    assertEquals(mass, Reflection.getFieldValue(Body.class, massField.getName(), b));

    for (Vector2D v : List.of(new Vector2D(xp, yp), new Vector2D(xv, yv))) {
      Stream<Field> vectorFields = Reflection.getFields(Body.class).filter(Reflection.hasType(Vector2D.class));
      if (!vectorFields.anyMatch(f -> Reflection.getFieldValue(Body.class, f.getName(), b).equals(v))) {
        fail("None of the fields is set to the value " + v);
      }
    }
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The NBody(double, double, double, double, double, String) constructor works correctly")
  @ParameterizedTest(name = "NBody({arguments}) populates all instance variables correctly")
  @CsvSource({
          "1, 2, 3, 4, 5, name",
          "1, 2, 3, 4, 5, ",
          "1.1, 2.2, 3.3, 4.4, 5.5, þú"
  })
  public void testConstructor1(double xp, double yp, double xv, double yv, double mass, String filename) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class, double.class, String.class);
    testConstructorFor(Reflection.newInstance(c, xp, yp, xv, yv, mass, filename), xp, yp, xv, yv, mass, filename);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The NBody(Vector2D, Vector2D, double, String) constructor works correctly")
  @ParameterizedTest(name = "NBody(<{0}, {1}>, <{2}, {3}>, {4}, {5}) populates all instance variables correctly")
  @CsvSource({
          "<1 2>, <3 4>, 5, name",
          "<1.1 2.2>, <3.3 4.4>, 5.5, þú"
  })
  public void testConstructor2(Vector2D pos, Vector2D vel, double mass, String filename) {
    Constructor c = Reflection.getConstructor(Body.class, Vector2D.class, Vector2D.class, double.class, String.class);
    testConstructorFor(Reflection.newInstance(c, pos, vel, mass, filename), pos.getX(), pos.getY(), vel.getX(), vel.getY(), mass, filename);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("The NBody(double, double, double, double) constructor works correctly")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}) populates all instance variables correctly")
  @CsvSource({
          "1, 2, 3, 4",
          "1.1, 2.2, 3.3, 4.4"
  })
  public void testConstructor3(double xp, double yp, double xv, double yv) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class);
    testConstructorFor(Reflection.newInstance(c, xp, yp, xv, yv), xp, yp, xv, yv, 0, null);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("distanceTo is calculated correctly")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}).distanceTo(NBody({4}, {5}, {6}, {7}) == {8}")
  @CsvSource({
          "0, 0, 0, 0, 1, 1, 1, 1, 1.4142135623730951",
          "1, 1, 5.0, 5.0, 0, 0, 2, 2, 1.4142135623730951",
          "1, 0, 0, 0, 1, 0, 1, 1, 0.0",
          "0, 1, 0, 0, 0, 1, 1, 1, 0.0",
          "1, 0, 0, 0, 0, 1, 1, 1, 1.4142135623730951",
          "0, 1, 0, 0, 1, 0, 1, 1, 1.4142135623730951",
          "0, 1, 0, 0, 0, 1, 1, 1, 0.0",
          "0, 1, 2, 3, 4, 5, 6, 7, 5.656854249492381"
  })
  public void testDistanceTo(double xp1, double yp1, double xv1, double yv1, double xp2, double yp2, double xv2, double yv2, double expected) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class);
    Body b1 = Reflection.newInstance(c, xp1, yp1, xv1, yv1);
    Body b2 = Reflection.newInstance(c, xp2, yp2, xv2, yv2);
    Method distanceTo = Reflection.getMethod(Body.class, "distanceTo", Body.class);
    assertEquals(expected, Reflection.invoke(distanceTo, b1, b2), EQUALITY_THRESHOLD);
  }

  @Order(0)
  @Tag("B")
  @DisplayName("getForceFrom is calculated correctly")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}, {4}, \"\").getForceFrom(NBody({5}, {6}, {7}, {8}, {9}, \"\") == {10}")
  @CsvSource({
          "0, 0, 0, 0, 1, 1, 1, 1, 1, 6, <-1.4149206691542813E-10 -1.4149206691542813E-10>",
          "1, 1, 5.0, 5.0, 2, 0, 0, 2, 2, 5, <2.3582011152571354E-10 2.3582011152571354E-10>",
          "1, 0, 0, 0, 3, 1, 0, 1, 1, 4, <NaN NaN>",
          "0, 1, 0, 0, 4, 0, 1, 1, 1, 3, <NaN NaN>",
          "1, 0, 0, 0, 5, 0, 1, 1, 1, 2, <2.3582011152571354E-10 -2.3582011152571354E-10>",
          "0, 1, 0, 0, 6, 1, 0, 1, 1, 1, <-1.4149206691542813E-10 1.4149206691542813E-10>",
          "0, 1, 1, 1, 6, 1, 0, 1, 1, 1, <-1.4149206691542813E-10 1.4149206691542813E-10>",
          "0, 1, 2, 3, 4, 5, 6, 7, 8, 9, <-3.3958096059702754E-11 -3.3958096059702754E-11>"
  })
  public void testGetForceFrom(double xp1, double yp1, double xv1, double yv1, double mass1, double xp2, double yp2, double xv2, double yv2, double mass2, Vector2D expected) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class, double.class, String.class);
    Body b1 = Reflection.newInstance(c, xp1, yp1, xv1, yv1, mass1, "");
    Body b2 = Reflection.newInstance(c, xp2, yp2, xv2, yv2, mass2, "");
    Method getForceFrom = Reflection.getMethod(Body.class, "getForceFrom", Body.class);
    assertEquals(expected, Reflection.invoke(getForceFrom, b2, b1));
  }

  @Order(0)
  @Tag("B")
  @DisplayName("toString() is correct")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}, {4}, {5}).toString() is correct")
  @CsvSource({
          "0, 0, 0, 0, 1, ,' 0.0000e+00  0.0000e+00  0.0000e+00  0.0000e+00  1.0000e+00         null'",
          "1.234567890123456789, 9876543210987654321, 1, 1, 987654321.0123456789,,' 1.2346e+00  9.8765e+18  1.0000e+00  1.0000e+00  9.8765e+08         null'",
          "1.234567890123456789, 9876543210987654321, 1, 1, 987654321.0123456789, test.png,' 1.2346e+00  9.8765e+18  1.0000e+00  1.0000e+00  9.8765e+08     test.png'",
  })
  public void testToString(double xp, double yp, double xv, double yv, double mass, String filename, String expected) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class, double.class, String.class);
    Body b = Reflection.newInstance(c, xp, yp, xv, yv, mass, filename);
    assertEquals(expected, b.toString());
  }

  @Order(0)
  @Tag("B")
  @DisplayName("getFileName() is correct")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}, {4}, {5}).getFileName() == {6}")
  @CsvSource({
          "0, 0, 0, 0, 1, ,",
          "0, 0, 0, 0, 1, null, null",
          "0, 0, 0, 0, 1, a.gif, a.gif",
          "0, 0, 0, 0, 1, hello.pdf, hello.pdf",
  })
  public void testGetFileName(double xp, double yp, double xv, double yv, double mass, String filename, String expected) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class, double.class, String.class);
    Body b = Reflection.newInstance(c, xp, yp, xv, yv, mass, filename);
    assertEquals(expected, b.getFileName());
  }

  @Order(0)
  @Tag("B")
  @DisplayName("getCurrentPosition() is correct")
  @ParameterizedTest(name = "NBody({0}, {1}, {2}, {3}, {4}, {5}).getCurrentPosition() == {6}")
  @CsvSource({
          "0, 0, 0, 0, 1, null, <0 0>",
          "1, 2, 3, 4, 5, null, <1 2>",
          "5, 6, 7, 8, 9, null, <5 6>",
  })
  public void testGetCurrentPosition(double xp, double yp, double xv, double yv, double mass, String filename, Vector2D expected) {
    Constructor c = Reflection.getConstructor(Body.class, double.class, double.class, double.class, double.class, double.class, String.class);
    Body b = Reflection.newInstance(c, xp, yp, xv, yv, mass, filename);
    assertEquals(expected, b.getCurrentPosition());
  }

  @Order(2)
  @Tag("B")
  @DisplayName("calculateNewForceFrom() and updatePosition() are correct")
  @Test
  public void testBodyForceUpdate() {
    Body b = (Body)NBodySimulationTests.getBody(Body.class, 1.4960e+11, 0.0000e+00, 0.0000e+00, 2.9800e+04, 5.9740e+24, "earth.gif");
    Body[] bodies = new Body[] {
            b,
            (Body)NBodySimulationTests.getBody(Body.class, 2.2790e+11, 0.0000e+00, 0.0000e+00, 2.4100e+04, 6.4190e+23, "mars.gif"),
            (Body)NBodySimulationTests.getBody(Body.class, 5.7900e+10, 0.0000e+00, 0.0000e+00, 4.7900e+04, 3.3020e+23, "mercury.gif"),
            (Body)NBodySimulationTests.getBody(Body.class, 0.0000e+00, 0.0000e+00, 0.0000e+00, 0.0000e+00, 1.9890e+30, "sun.gif"),
            (Body)NBodySimulationTests.getBody(Body.class, 1.0820e+11, 0.0000e+00, 0.0000e+00, 3.5000e+04, 4.8690e+24, "venus.gif")
    };

    Vector2D force = new Vector2D(-3.5414100082049765E22, 0.0);
    b.calculateNewForceFrom(bodies);
    Stream<Field> vectorFields = Reflection.getFields(Body.class).filter(Reflection.hasType(Vector2D.class));
    if (!vectorFields.anyMatch(f -> ((Vector2D)Reflection.getFieldValue(Body.class, f.getName(), b)).equals(force))) {
      fail("On the first call to calculateNewForceFrom(), none of the fields is set to the value " + force);
    }

    b.calculateNewForceFrom(bodies);
    vectorFields = Reflection.getFields(Body.class).filter(Reflection.hasType(Vector2D.class));
    if (!vectorFields.anyMatch(f -> ((Vector2D)Reflection.getFieldValue(Body.class, f.getName(), b)).equals(force))) {
      fail("On the second call to calculateNewForceFrom(), none of the fields is set to the value " + force);
    }

    double dt = 1001;
    Vector2D expectedPosition = new Vector2D(1.4959999406009982E11, 2.98298E7);
    b.updatePosition(dt);
    assertEquals(expectedPosition, b.getCurrentPosition(), "updatePosition() does not correctly update after force calculation");
  }
}