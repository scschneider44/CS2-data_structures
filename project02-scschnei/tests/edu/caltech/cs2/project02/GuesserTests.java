package edu.caltech.cs2.project02;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.project02.guessers.AIHangmanGuesser;
import org.junit.jupiter.api.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;

public class GuesserTests {
    private static String GUESSER_SOURCE = "src/edu/caltech/cs2/project02/guessers/AIHangmanGuesser.java";

    @Order(1)
    @Tag("A")
    @DisplayName("Does not use or import disallowed classes")
    @Test
    public void testForInvalidClasses() {
        List<String> regexps = List.of("java.lang.reflect", "Arrays");
        Inspection.assertNoImportsOf(GUESSER_SOURCE, regexps);
        Inspection.assertNoUsageOf(GUESSER_SOURCE, regexps);
    }

    @Order(1)
    @Tag("A")
    @DisplayName("AIHangmanGuesser has no instance fields")
    @Test
    public void testNoFields() {
        Reflection.assertFieldsLessThan(AIHangmanGuesser.class, "private", 1);
    }
    @Order(1)

    @Tag("A")
    @DisplayName("Test that the dictionary is static")
    @Test
    public void testDictionaryModifiers() {
        Field dictField = Reflection.getFieldByType(AIHangmanGuesser.class, String.class);
        Reflection.checkFieldModifiers(dictField, List.of("private", "static"));
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test getGuess Method in AIHangmanGuesser")
    @Test
    public void testGetGuess() {
        Constructor c = Reflection.getConstructor(AIHangmanGuesser.class);
        AIHangmanGuesser instance = Reflection.newInstance(c);
        Method m = Reflection.getMethod(AIHangmanGuesser.class,"getGuess", String.class, Set.class);

        // test character with most occurrences is chosen
        assertEquals('e', (char)Reflection.invoke(m,instance, "---", new HashSet<>(Arrays.asList('a'))));
        assertEquals('i', (char)Reflection.invoke(m, instance, "---", new HashSet<>(Arrays.asList('a', 'e', 'o'))));
        assertEquals('e', (char)Reflection.invoke(m, instance, "sc--nc-", new HashSet<>(Arrays.asList('s', 'n', 'c'))));
        // test first character in alphabetical order is chosen
        assertEquals('b', (char)Reflection.invoke(m, instance, "-ee", new HashSet<>(Arrays.asList('e'))));
        assertEquals('a', (char)Reflection.invoke(m, instance, "-ppl-", new HashSet<>(Arrays.asList('p', 'l'))));
        // test only correct letter is chosen
        assertEquals('g', (char)Reflection.invoke(m, instance, "en-ineerin-", new HashSet<>(Arrays.asList('e', 'n', 'i', 'r'))));
    }

}
