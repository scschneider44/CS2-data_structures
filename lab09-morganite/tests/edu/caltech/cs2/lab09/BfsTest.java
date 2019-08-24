package edu.caltech.cs2.lab09;

import edu.caltech.cs2.helpers.CaptureSystemOutput;
import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.libraries.StdDraw;
import edu.caltech.cs2.helpers.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;

@CaptureSystemOutput
public class BfsTest {
    @Tag("B")
    @Test
    @DisplayName("BFS Iterative Check")
    public void IBfsCheck() {
        String srcPath = "src/edu/caltech/cs2/lab09/Maze.java";
        Method solveMethod = Reflection.getMethod(Maze.class, "solveBFS");
        Inspection.assertIterative(srcPath, solveMethod);
    }

    @Tag("B")
    @Test
    @DisplayName("BFS Solver Correctness Test 1")
    public void BfsCorrectnessTest1 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/Maze1.txt";
        String acceptablePath = "tests/data/BTestSlns/B1Sln.txt";
        Maze maze = new Maze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("B")
    @Test
    @DisplayName("BFS Solver Correctness Test 2")
    public void BfsCorrectnessTest2 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/Maze2.txt";
        String acceptablePath = "tests/data/BTestSlns/B2Sln.txt";
        Maze maze = new Maze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("B")
    @Test
    @DisplayName("BFS Solver Correctness Test 3")
    public void BfsCorrectnessTest3 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/Maze3.txt";
        String acceptablePath = "tests/data/BTestSlns/B3Sln.txt";
        Maze maze = new Maze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("B")
    @Test
    @DisplayName("BFS Solver Correctness Test 4")
    public void BfsCorrectnessTest4 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/Maze4.txt";
        String acceptablePath = "tests/data/BTestSlns/B4Sln.txt";
        Maze maze = new Maze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("B")
    @Test
    @DisplayName("BFS Solver Correctness Test 5")
    public void BfsCorrectnessTest5 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/Maze5.txt";
        String acceptablePath = "tests/data/BTestSlns/B5Sln.txt";
        Maze maze = new Maze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Iterative Check")
    public void OBfsIterativeCheck() {
        String srcPath = "src/edu/caltech/cs2/lab09/OpenMaze.java";
        Method solveMethod = Reflection.getMethod(OpenMaze.class, "solveGraphBFS");
        Inspection.assertIterative(srcPath, solveMethod);
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Solver Correctness Test 1")
    public void GraphBfsCorrectnessTest1 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/OMaze1.txt";
        String acceptablePath = "tests/data/ATestSlns/A1Sln.txt";
        OpenMaze maze = new OpenMaze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveGraphBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Solver Correctness Test 2")
    public void GraphBfsCorrectnessTest2 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/OMaze2.txt";
        String acceptablePath = "tests/data/ATestSlns/A2Sln.txt";
        OpenMaze maze = new OpenMaze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveGraphBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Solver Correctness Test 3")
    public void GraphBfsCorrectnessTest3 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/OMaze3.txt";
        String acceptablePath = "tests/data/ATestSlns/A3Sln.txt";
        OpenMaze maze = new OpenMaze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveGraphBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Solver Correctness Test 4")
    public void GraphBfsCorrectnessTest4 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/OMaze4.txt";
        String acceptablePath = "tests/data/ATestSlns/A4Sln.txt";
        OpenMaze maze = new OpenMaze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveGraphBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }

    @Tag("A")
    @Test
    @DisplayName("Graph BFS Solver Correctness Test 5")
    public void GraphBfsCorrectnessTest5 (CaptureSystemOutput.OutputCapture capture) throws IOException {
        String mazePath = "tests/data/Mazes/OMaze5.txt";
        String acceptablePath = "tests/data/ATestSlns/A5Sln.txt";
        OpenMaze maze = new OpenMaze(30, mazePath);
        StdDraw.enableDoubleBuffering();
        maze.draw();

        maze.done = false;
        maze.solveGraphBFS();
        StdDraw.clear();

        String out = capture.toString().replace("\r\n", "\n").strip();
        String sln = new String(Files.readAllBytes(Paths.get(acceptablePath)));
        sln = sln.replace("\r\n", "\n").strip();
        assertEquals(sln, out, "Output log is not correct. Solver is either not correct or not BFS.");
    }
}