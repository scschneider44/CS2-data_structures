package edu.caltech.cs2.lab01;

import edu.caltech.cs2.helpers.CaptureSystemOutput;
import edu.caltech.cs2.helpers.FileSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CaptureSystemOutput
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikipediaMainTests {
  @Order(0)
  @Tag("A")
  @DisplayName("Test Main")
  @ParameterizedTest(name = "The arguments \"{0}\" result in the right output")
  @FileSource(inputs = "Satellite_galaxies_of_the_Milky_Way", outputFiles = "satellite_galaxies_of_the_milky_way_main_output.txt")
  public void testMain(String arguments, String expectedOutput, CaptureSystemOutput.OutputCapture capture) {
    Main.main(arguments.split("\\|"));
    assertEquals(expectedOutput.strip(), capture.toString().strip());
  }
}