package edu.caltech.cs2.lab04;

import edu.caltech.cs2.helpers.CaptureSystemOutput;
import edu.caltech.cs2.helpers.FileSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@CaptureSystemOutput
public class QuestionTreeTests {
    @Order(0)
    @Tag("A")
    @DisplayName("Testing Play()")
    @ParameterizedTest(name = "Test play on QuestionTree constructed from input {0} against expected trace {1}")
    @FileSource(
            inputs = {"trace0-input.txt",
                    "trace2-input.txt",
            },
            outputFiles = {"trace0.txt",
                    "trace2.txt",
            })
    public void testPlay(String filename, String expectedOutput, CaptureSystemOutput.OutputCapture capture){
        final InputStream systemIn = System.in;
        // The file that the QuestionTree will be constructed from
        String inputFile = null;
        // The input to be read as the QuestionTree game is playing
        String input = "";
        Scanner s = null;
        try {
            s = new Scanner(new File("tests/inputs/" + filename));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/inputs/" + filename + "with a Scanner");
        }

        if (s.hasNextLine()) {
            inputFile = s.nextLine();
        }
        while (s.hasNextLine()) {
            input = input + s.nextLine() + "\n";
        }
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        try {
            s = new Scanner(new File("tests/inputs/" + inputFile));
        } catch (java.io.FileNotFoundException e) {
            fail("Could not open file tests/inputs/" + inputFile + "with a Scanner");
        }
        QuestionTree tree = new QuestionTree(s);
        tree.play();

        assertEquals(expectedOutput.replace("\r\n", "\n").strip(), capture.toString().replace("\r\n", "\n").strip());

        System.setIn(systemIn);
    }
}
