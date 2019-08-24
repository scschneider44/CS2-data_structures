package edu.caltech.cs2.lab01;

import edu.caltech.cs2.helpers.FileSource;
import edu.caltech.cs2.helpers.Reflection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikipediaPageTests {
  @Order(0)
  @Tag("C")
  @DisplayName("getTitle")
  @ParameterizedTest(name = "\"{0}\" results in a title of \"{1}\"")
  @CsvSource({
          "Phineas_Gage, Phineas Gage",
          "Sombrero_Galaxy, Sombrero Galaxy",
          "Sunflower_Galaxy, Sunflower Galaxy",
          "Messier 63, Messier 63",
          "Pizza_farm, Pizza Farm",
          "Pizza_fArm, Pizza Farm",
          "Pizza farm, Pizza Farm",
          "pizZa_farm, Pizza Farm"
  })
  public void testGetTitle(String title, String correct) {
    WikipediaPage page = new WikipediaPage(title);
    assertEquals(correct, page.getTitle());
  }

  @Order(1)
  @Tag("C")
  @DisplayName("Test convertToTitleCase Method Signature")
  @Test
  public void testModifiersOnConvertToTitleCase() {
    Method convertToTitleCase = null;
    try {
      convertToTitleCase = WikipediaPage.class.getDeclaredMethod("convertToTitleCase", String.class);
    } catch (NoSuchMethodException e) {
      fail("Could not find the 'convertToTitleCase' method");
    }
    if ((convertToTitleCase.getModifiers() & Modifier.PRIVATE) == 0) {
      fail("'convertToTitleCase' method should be private");
    }
    if ((convertToTitleCase.getModifiers() & Modifier.STATIC) == 0) {
      fail("'convertToTitleCase' method should be static");
    }
  }

  @Order(2)
  @Tag("B")
  @DisplayName("getText")
  @ParameterizedTest(name = "\"{0}\" results in the right text")
  @FileSource(
          inputs = {"Sandycove_Island", "Selagama_Colony"},
          outputFiles = {"sandycove_island.txt", "selagama_colony.txt"}
  )
  public void testGetText(String title, String expectedOutput) {
    WikipediaPage page = new WikipediaPage(title);
    assertEquals(expectedOutput.replace("\r\n", "\n"), page.getText());
  }

  @Order(3)
  @Tag("B")
  @DisplayName("isValid")
  @ParameterizedTest(name = "\"{0}\" {1} a valid wikipedia page")
  @CsvSource({
          "Phineas_Gage, is",
          "Sombrero_Galaxy, is",
          "Sunflower_Galaxy, is",
          "Messier 63, is",
          "Pizza_farm, is",
          "Pizza farm, is",
          "Pizza Farm, is",
          "notavalidpage, is not"
  })
  public void testIsValid(String title, String result) {
    WikipediaPage page = new WikipediaPage(title);
    assertEquals(result.equals("is"), page.isValid());
  }

  @Order(4)
  @Tag("B")
  @DisplayName("isRedirect")
  @ParameterizedTest(name = "\"{0}\" {1} a redirect page")
  @CsvSource({
          "Phineas_Gage, is not",
          "Pizza_farm, is not",
          "Sombrero_Galaxy, is not",
          "Sunflower_Galaxy, is",
          "Messier_63, is not",
          "Southern_Pinwheel_Galaxy, is"
  })
  public void testIsRedirect(String title, String result) {
    WikipediaPage page = new WikipediaPage(title, false);
    assertEquals(result.equals("is"), page.isRedirect());
  }

  @Order(5)
  @Tag("B")
  @DisplayName("isGalaxy")
  @ParameterizedTest(name = "\"{0}\" {1} a galaxy")
  @CsvSource({
          "Phineas_Gage, is not",
          "Pizza_farm, is not",
          "Sombrero_Galaxy, is",
          "Sunflower_Galaxy, is",
          "Messier_63, is",
          "bananaramarama, is not"
  })
  public void testIsGalaxy(String title, String result) {
    WikipediaPage page = new WikipediaPage(title);
    assertEquals(result.equals("is"), page.isGalaxy());
  }

  @Order(6)
  @Tag("A")
  @DisplayName("Link Iteration")
  @ParameterizedTest(name = "Links for \"{0}\" are correct")
  @FileSource(
          inputs = {"Pizza farm", "RSA (cryptosystem)"},
          outputFiles = {"links_for_pizza_farm.txt", "links_for_rsa.txt"}
  )
  public void testLinkIteration(String title, String correctLinks) throws Throwable {
    WikipediaPage page = new WikipediaPage(title);
    Method nextLink = Reflection.getPrivateMethod(WikipediaPage.class, "nextLink");

    List<String> myLinks = new ArrayList<>();
    String link = Reflection.invoke(nextLink, page);
    while (link != null) {
      myLinks.add(link);
      link = Reflection.invoke(nextLink, page);
    }
    assertIterableEquals(Arrays.asList(correctLinks.replace("\r\n", "\n").split("\n")), myLinks);
  }

  @Order(7)
  @Tag("A")
  @DisplayName("getTitle2")
  @ParameterizedTest(name = "\"{0}\" results in a title of \"{1}\"")
  @CsvSource({
          "Ursa Major II, Ursa Major II",
          "Ursa Major iI, Ursa Major II",
          "Ursa Major ii, Ursa Major II",
          "Ursa Major Ii, Ursa Major II",
          "Leo II (dwarf galaxy), Leo II (dwarf galaxy)",
          "Leo II (HeLlo GALAXY), Leo II (hello galaxy)"
  })
  public void testGetTitle2(String title, String correct) {
    WikipediaPage page = new WikipediaPage(title);
    assertEquals(correct, page.getTitle());
  }
}