package edu.caltech.cs2.lab04;

import edu.caltech.cs2.helpers.CaptureSystemOutput;
import edu.caltech.cs2.helpers.FileSource;
import edu.caltech.cs2.helpers.Reflection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.Scanner;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@CaptureSystemOutput
public class FullStringTreeTests {
    private void testStringNodeEquality(FullStringTree.StringNode expected, FullStringTree.StringNode actual){
        assertNotNull(expected);
        assertNotNull(actual);
        if (expected.left != null){
            assertNotNull(actual.left);
            testStringNodeEquality(expected.left, actual.left);
        } if (actual.left != null) {
            assertNotNull(expected.left);
        }
        if (expected.right != null){
            assertNotNull(actual.right);
            testStringNodeEquality(expected.right, actual.right);
        } if (actual.right != null){
            assertNotNull(expected.right);
        }
        assertEquals(expected.data, actual.data);
    }

    @Order(0)
    @Tag("C")
    @DisplayName("Testing QuestionTree Constructor on tests/data/fruit.txt")
    @Test
    public void testFullStringTreeConstructorFruit(){
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/data/fruit.txt"));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/data/fruit.txt with a Scanner");
        }
        FullStringTree tree = new FullStringTree(s);
        String rootFieldName = Reflection.getFieldByType(FullStringTree.class, FullStringTree.StringNode.class).getName();
        FullStringTree.StringNode actual = Reflection.getFieldValue(FullStringTree.class, rootFieldName, tree);

        FullStringTree.StringNode leftnode = new FullStringTree.StringNode("pineapple");
        FullStringTree.StringNode rightnode = new FullStringTree.StringNode("kiwi");
        leftnode = new FullStringTree.StringNode("orange", leftnode, rightnode);
        FullStringTree.StringNode otherleftnode = new FullStringTree.StringNode("watermelon");
        rightnode = new FullStringTree.StringNode("strawberry");
        otherleftnode = new FullStringTree.StringNode("lemon", otherleftnode, rightnode);
        rightnode = new FullStringTree.StringNode("mango");
        rightnode = new FullStringTree.StringNode("lime", otherleftnode, rightnode);
        FullStringTree.StringNode expected = new FullStringTree.StringNode("apple", leftnode, rightnode);

        this.testStringNodeEquality(expected, actual);
    }

    @Order(0)
    @Tag("C")
    @DisplayName("Testing QuestionTree Constructor on tests/data/soda.txt")
    @Test
    public void testFullStringTreeConstructorSoda(){
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/data/soda.txt"));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/data/soda.txt with a Scanner");
        }
        FullStringTree tree = new FullStringTree(s);
        String rootFieldName = Reflection.getFieldByType(FullStringTree.class, FullStringTree.StringNode.class).getName();
        FullStringTree.StringNode actual = Reflection.getFieldValue(FullStringTree.class, rootFieldName, tree);

        FullStringTree.StringNode leftnode = new FullStringTree.StringNode("Coke");
        FullStringTree.StringNode rightnode = new FullStringTree.StringNode("Root Beer");
        leftnode = new FullStringTree.StringNode("Mountain Dew", leftnode, rightnode);
        rightnode = new FullStringTree.StringNode("Ginger Ale");
        leftnode = new FullStringTree.StringNode("Sprite", leftnode, rightnode);
        FullStringTree.StringNode otherleftnode = new FullStringTree.StringNode("Sunkist");
        rightnode = new FullStringTree.StringNode("Pepsi");
        rightnode = new FullStringTree.StringNode("Fanta", otherleftnode, rightnode);
        FullStringTree.StringNode expected = new FullStringTree.StringNode("Dr. Pepper", leftnode, rightnode);

        this.testStringNodeEquality(expected, actual);
    }

    @Order(0)
    @Tag("B")
    @DisplayName("Testing Explore()")
    @ParameterizedTest(name = "Test Explore on FullStringTree constructed from {0}")
    @CsvSource({"fruit.txt",
                "soda.txt"})
    public void testExplore(String filename){
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/data/" + filename));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/data/" + filename + "with a Scanner");
        }
        FullStringTree tree = new FullStringTree(s);
        List<String> actual = tree.explore();
        s.close();
        try {
            s = new Scanner(new File("tests/data/" + filename));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/data/" + filename + "with a Scanner");
        }
        List<String> expected = new ArrayList<>();
        while(s.hasNextLine()){
            expected.add(s.nextLine());
        }
        s.close();
        assertEquals(expected, actual);
    }

    @Order(0)
    @Tag("B")
    @DisplayName("Testing Serialize()")
    @ParameterizedTest(name = "Test Serialize on FullStringTree constructed from {0} and {1}")
    @FileSource(
            inputs = {"fruit.txt",
                      "soda.txt",
            },
            outputFiles = {"fruit.txt",
                           "soda.txt",})
    public void testSerialize(String filename, String expectedOutput, CaptureSystemOutput.OutputCapture capture){
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/data/" + filename));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/data/" + filename + "with a Scanner");
        }
        FullStringTree tree = new FullStringTree(s);
        tree.serialize(System.out);
        s.close();
        assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());
    }
}
