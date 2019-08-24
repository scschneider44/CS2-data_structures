package edu.caltech.cs2.lab06;

import edu.caltech.cs2.helpers.CaptureSystemOutput;
import edu.caltech.cs2.helpers.FileSource;
import edu.caltech.cs2.helpers.Reflection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@CaptureSystemOutput
public class PhraseAnagramsTests {

  private static List getDictionary() throws FileNotFoundException {
    Scanner input = new Scanner(new File("dictionaries/dictionary.txt"));

    // Read dictionary into a List
    List<String> dictionary = new ArrayList<String>();
    while (input.hasNextLine()) {
      dictionary.add(input.nextLine());
    }
    return dictionary;
  }

  private static List getDictionaryEleven() throws FileNotFoundException {
    Scanner input = new Scanner(new File("dictionaries/eleven.txt"));

    // Read dictionary into a List
    List<String> dictionary = new ArrayList<String>();
    while (input.hasNextLine()) {
      dictionary.add(input.nextLine());
    }
    return dictionary;
  }

  @Order(1)
  @Tag("B")
  @DisplayName("AnagramGenerator has no instance fields")
  @Test
  public void testNoFields() {
    Reflection.assertFieldsLessThan(AnagramGenerator.class, "private", 1);
    Reflection.assertFieldsLessThan(AnagramGenerator.class, "public", 1);
  }

  @Order(2)
  @Tag("B")
  @DisplayName("Test printPhrases method in AnagramGenerator")
  @ParameterizedTest(name = "{0}")
  @FileSource(
          inputs = {
                  "meat",
                  "listen",
                  "magikarp",
                  "qwertyuiop",
          },
          outputFiles = {
                  "phrase_meat.txt",
                  "phrase_listen.txt",
                  "phrase_magikarp.txt",
                  "phrase_qwertyuiop.txt",
          }
  )
  public void testPrintPhrases(String word, String expectedOutput, CaptureSystemOutput.OutputCapture capture) {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionary();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    AnagramGenerator.printPhrases(word, dictionary);
    assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());
  }

  @Order(3)
  @Tag("B")
  @DisplayName("Test printPhrases method on empty string")
  @Test
  public void testPrintPhrasesEmpty(CaptureSystemOutput.OutputCapture capture) {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionary();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    AnagramGenerator.printPhrases("", dictionary);
    assertEquals("[]", capture.toString().replace("\r\n", "\n").strip());
  }

  @Order(4)
  @Tag("B")
  @DisplayName("Test printPhrases method on null string")
  @Test
  public void testPrintPhrasesNull() {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionary();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    final List finalDictionary = dictionary;
    assertThrows(NullPointerException.class, ()->{AnagramGenerator.printPhrases(null, finalDictionary);});
  }

  @Order(5)
  @Tag("B")
  @DisplayName("Test printPhrases method on string with a space")
  @Test
  public void testPrintPhrasesSpace(CaptureSystemOutput.OutputCapture capture) {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionary();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    AnagramGenerator.printPhrases(" ", dictionary);
    assertEquals("[]", capture.toString().replace("\r\n", "\n").strip());
  }

  @Order(6)
  @Tag("B")
  @DisplayName("Test printPhrases method on string with no anagrams")
  @Test
  public void testPrintPhrasesNone(CaptureSystemOutput.OutputCapture capture) {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionary();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    AnagramGenerator.printPhrases("xyz", dictionary);
    assertEquals("", capture.toString().replace("\r\n", "\n").strip());
  }

  @Order(7)
  @Tag("B")
  @DisplayName("Test printPhrases method on a phrase")
  @ParameterizedTest(name = "{0}")
  @FileSource(
          inputs = {
                  "eleven plus two",
                  "two plus eleven",
          },
          outputFiles = {
                  "phrase_eleven.txt",
                  "phrase_eleven.txt",
          }
  )
  public void testPrintWordsEleven(String word, String expectedOutput, CaptureSystemOutput.OutputCapture capture) {
    // Load the dictionary from the file
    List dictionary = null;
    try {
      dictionary = getDictionaryEleven();
    } catch (FileNotFoundException e) {
      fail("Dictionary file not found");
    }

    AnagramGenerator.printPhrases(word, dictionary);
    assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());
  }
}