package edu.caltech.cs2.project01;

import edu.caltech.cs2.helpers.*;
import edu.caltech.cs2.libraries.FakeBody;
import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.StdDraw;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@CaptureSystemOutput
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NBodySimulationTests {

  private static String SIMULATION_SOURCE ="src/edu/caltech/cs2/project01/NBodySimulation.java";

  @Order(0)
  @Tag("C")
  @DisplayName("Does not use or import disallowed classes")
  @Test
  public void testForInvalidClasses() {
    List<String> regexps = List.of("java.util(?!.Scanner)", "java.lang.reflect");
    Inspection.assertNoImportsOf(SIMULATION_SOURCE, regexps);
    Inspection.assertNoUsageOf(SIMULATION_SOURCE, regexps);
  }

  private void testVisual(IBody[] bodies, double radius, String correctFile) {
    StdDraw.showFrame(false);
    Method setupDrawing = Reflection.getMethod(NBodySimulation.class, "setupDrawing", double.class);
    Reflection.invokeStatic(setupDrawing, radius);
    Method drawStep = Reflection.getMethod(NBodySimulation.class, "drawStep", IBody[].class);
    Reflection.invokeStatic(drawStep, (Object) bodies);
    BufferedImage expectedOutput = Images.getImage("tests/data/" + correctFile);
    Images.assertImagesEqual(expectedOutput, StdDraw.getImage(), 2);
  }

  public static IBody getBody(Class<?> bodyClazz, double xPos, double yPos, double xVel, double yVel, double mass, String filename) {
    try {
      return (IBody)bodyClazz.getConstructor(double.class, double.class, double.class, double.class, double.class, String.class).
              newInstance(xPos, yPos, xVel, yVel, mass, filename);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      fail("Could not instantiate IBody class: " + bodyClazz.getName());
      return null;
    }
  }

  abstract class BTests {
    private Class<?> BodyClass;

    public BTests(Class <?> clazz) {
      this.BodyClass = clazz;
    }

    @Order(0)
    @DisplayName("Test printState() with no planets")
    @ParameterizedTest(name = "Test radius {0}")
    @FileSource(
            inputs = {
                    "2.50e+11",
                    "2.5555555555",
                    "2.555555555555e+100"
            },
            outputFiles = {
                    "printState-out-1.txt",
                    "printState-out-2.txt",
                    "printState-out-3.txt",
            }
    )
    public void testPrintStateNoPlanets(String argument, String expected,  CaptureSystemOutput.OutputCapture capture) {
      IBody[] bodies = (IBody[])Array.newInstance(this.BodyClass, 0);
      Method printState = Reflection.getMethod(NBodySimulation.class, "printState", double.class, IBody[].class);
      Reflection.invokeStatic(printState, Double.parseDouble(argument), bodies);
      assertEquals(expected.replace("\r\n", "\n"), capture.toString().replace("\r\n", "\n"));
    }

    @Order(1)
    @DisplayName("Test visual no planets first frame")
    @Test
    public void testMainVisualNoPlanets() {
      testVisual((IBody[])Array.newInstance(this.BodyClass, 0), 2.50e+11, "empty.png");
    }

    @Order(2)
    @DisplayName("Test visual planets.txt first frame")
    @Test
    public void testMainVisualInputPlanets() {
      IBody[] bodies = new IBody[] {
          getBody(this.BodyClass, 1.4960e+11, 0.0000e+00, 0.0000e+00, 2.9800e+04, 5.9740e+24, "earth.gif"),
          getBody(this.BodyClass, 2.2790e+11, 0.0000e+00, 0.0000e+00, 2.4100e+04, 6.4190e+23, "mars.gif"),
          getBody(this.BodyClass, 5.7900e+10, 0.0000e+00, 0.0000e+00, 4.7900e+04, 3.3020e+23, "mercury.gif"),
          getBody(this.BodyClass, 0.0000e+00, 0.0000e+00, 0.0000e+00, 0.0000e+00, 1.9890e+30, "sun.gif"),
          getBody(this.BodyClass, 1.0820e+11, 0.0000e+00, 0.0000e+00, 3.5000e+04, 4.8690e+24, "venus.gif")
      };
      testVisual(bodies, 2.50e+11, "planets-initial.png");
    }


    public void testMainVisual(String arguments, BufferedImage expectedOutput) {
        StdDraw.setNoPause(true);
        Method main = Reflection.getMethod(NBodySimulation.class, "main", String[].class);
        Reflection.invokeStatic(main, (Object) arguments.split(" "));
        StdDraw.setNoPause(false);
        Images.assertImagesEqual(expectedOutput, StdDraw.getImage(), 2);
    }

    @Order(3)
    @DisplayName("Test NBodySimulation planets.txt (visual output)")
    @ParameterizedTest(name = "The arguments \"{0}\" result in the right visual output")
    @ImageFileSource(
            inputs = {
                    "157788000.0 25000.0 data/simulations/planets.txt",
            },
            outputFiles = {
                    "planets-final-0.png",
            }
    )
    public void testMainVisualEasy(String arguments, BufferedImage expectedOutput) {
      testMainVisual(arguments, expectedOutput);
    }
  }

  @Nested
  @Tag("C")
  @DisplayName("FakeBody Integration Tests")
  class FakeBodyTests extends BTests {
    public FakeBodyTests() {
      super(FakeBody.class);
    }
  }

  @Nested
  @Tag("A")
  @DisplayName("Body Integration Tests")
  class BodyTests extends BTests {
    public BodyTests() {
      super(Body.class);
    }
  }

  @Order(0)
  @Tag("C")
  @DisplayName("Test NBodySimulation planets.txt (text output only position)")
  @ParameterizedTest(name = "The arguments \"{0}\" result in the right textual output")
  @FileSource(
          inputs = {
                  "157788000.0 25000.0 data/simulations/planets.txt",
                  "127788000.0 25000.0 data/simulations/planets.txt",
                  "107788000.0 25000.0 data/simulations/planets.txt",
          },
          outputFiles = {
                  "planets-out-1.txt",
                  "planets-out-2.txt",
                  "planets-out-3.txt",
          }
  )
  public void testMainPosition(String arguments, String expectedOutput, CaptureSystemOutput.OutputCapture capture) {
      StdDraw.setDoNothing(true);
      Method main = Reflection.getMethod(NBodySimulation.class, "main", String[].class);
      Reflection.invokeStatic(main, (Object) arguments.split(" "));
      StdDraw.setDoNothing(false);
      List<String[]> expected = Stream.of(expectedOutput.strip().split("\\r?\\n")).map(x -> x.split("\\s")).collect(Collectors.toList());
      List<String[]> actual = Stream.of(capture.toString().strip().split("\\r?\\n")).map(x -> x.split("\\s")).collect(Collectors.toList());
      for (int i = 2; i < expected.size(); i++) {
        assertEquals(expected.get(i)[0], actual.get(i)[0], "The x-coordinate of the position of body " + (i - 2) + " was wrong");
        assertEquals(expected.get(i)[1], actual.get(i)[1], "The y-coordinate of the position of body " + (i - 2) + " was wrong");
      }
  }

  @Order(2)
  @Tag("A")
  @DisplayName("Test NBodySimulation all (text output all)")
  @ParameterizedTest(name = "The arguments \"{0}\" result in the right textual output")
  @FileSource(
          inputs = {
                  "157788000.0 25000.0 data/simulations/planets.txt",
                  "127788000.0 25000.0 data/simulations/planets.txt",
                  "107788000.0 25000.0 data/simulations/planets.txt",
                  "125000.0 125000.0 data/simulations/planets.txt",
                  "157788000.0 25000.0 data/simulations/1body.txt",
                  "15778800.0 20000.0 data/simulations/3body.txt",
                  "7788000.0 5000.0 data/simulations/awesome.txt"
          },
          outputFiles = {
                  "planets-out-1.txt",
                  "planets-out-2.txt",
                  "planets-out-3.txt",
                  "planets-out-4.txt",
                  "1body-out.txt",
                  "3body-out.txt",
                  "awesome-out.txt"
          }
  )
  public void testMainEverything(String arguments, String expectedOutput, CaptureSystemOutput.OutputCapture capture) {
      StdDraw.setDoNothing(true);
      Method main = Reflection.getMethod(NBodySimulation.class, "main", String[].class);
      Reflection.invokeStatic(main, (Object) arguments.split(" "));
      StdDraw.setDoNothing(false);
      assertEquals(expectedOutput.strip().replace("\r\n", "\n"), capture.toString().strip().replace("\r\n", "\n"));
  }
}
