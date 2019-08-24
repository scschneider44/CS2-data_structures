package edu.caltech.cs2.lab02;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BinarySearcherTests {

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class HandWrittenBinarySearcherTests {
        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class ProvidedLinearSearcherTests {

            /***
             * Tests that the array {1, 3, 5} behaves as {@code expected} on the
             * provided {@code needle} input.
             * @param needle the needle to feed to {@code linearSearch}
             * @param expected the expected output of {@code linearSearch}
             ***/
            public void test135Array(int needle, int expected) {
                int actual = Searcher.binarySearch(needle, new int[]{1, 3, 5});
                assertEquals(expected, actual);
            }

            @Test
            @Order(0)
            public void testDoesntExistBeginningOfArray() {
                test135Array(0, -1);
            }

            @Test
            @Order(1)
            public void testExistsBeginningOfArray() {
                test135Array(1, 0);
            }

            @Test
            @Order(2)
            public void testDoesntExistMiddleOfArray1() {
                test135Array(2, -2);
            }

            @Test
            @Order(3)
            public void testExistsMiddleOfArray1() {
                test135Array(3, 1);
            }

            @Test
            @Order(4)
            public void testDoesntExistMiddleOfArray2() {
                test135Array(4, -3);
            }

            @Test
            @Order(5)
            public void testExistsEndOfArray() {
                test135Array(5, 2);
            }

            @Test
            @Order(6)
            public void testDoesntExistEndOfArray() {
                test135Array(6, -4);
            }
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class StudentWrittenBinarySearcherTests {

            public void testEmptyArray(int needle, int expected) {
                int actual = Searcher.binarySearch(needle, new int[]{});
                assertEquals(expected, actual);
            }

            public void testSingleEArray(int needle, int expected) {
                int actual = Searcher.binarySearch(needle, new int[]{5});
                assertEquals(expected, actual);
            }

            public void testEvenArray(int needle, int expected) {
                int actual = Searcher.binarySearch(needle, new int[]{1, 3, 5, 7});
                assertEquals(expected, actual);
            }

            public void testUnsortedArray(int needle, int expected) {
                int actual = Searcher.binarySearch(needle, new int[] {4, 1, 3});
            }


            @Test
            @Order(7)
            public void testEmptyInputArray() {
                testEmptyArray(2, -1);
            }

            @Test
            @Order(8)
            public void testSingleItemInputArray() { testSingleEArray(5,0); }

            @Test
            @Order(9)
            public void testEvenLengthInputArray() {
                testEvenArray(6, -4);
            }

            @Test
            @Order(10)
            public void testNullInputArray() {
                // For this test, you'll want to use the assertThrows(...) method
                // instead of the assertEquals(...) method.
                assertThrows(NullPointerException.class, () -> {Searcher.linearSearch(4, null);});
            }

            @Test
            @Order(11)
            public void testUnsortedInputArray() {
                // For this test, you'll want to use the assertThrows(...) method
                // instead of the assertEquals(...) method.
                assertThrows(IllegalArgumentException.class, () -> Searcher.linearSearch(3, new int[] {3, 6, 2}));
            }
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class StressBinarySearcherTests {
        public int[] generateRandomSortedArray(Random r, int length) {
            int[] arr = new int[length];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = r.nextInt();
            }
            Arrays.sort(arr);
            return arr;
        }

        @Test
        @Order(0)
        public void testBinarySearchUsingLinearSearch() {
            // Choose a number of random arrays to generate
            int TRIALS = 49;

            // Initialize a single random object to use throughout.
            // We provide a constant "seed" to make the test deterministic.
            // The seed (in this case "1337") initializes the random number generator
            // to use a specific state.
            Random r = new Random(1337);

            int trial = 0;
            while (trial < TRIALS) {
                // Generate a random array of a random length
                int[] arr = generateRandomSortedArray(r, Math.abs(r.nextInt() % 10000));

                // We want to test both elements that are in the array and not in the array
                // So, run through all the elements in the array, and for each one test both
                // that element and one that is likely to not be in the array
                for (int x : arr) {
                    int y = r.nextInt();
                    assertEquals(Searcher.linearSearch(y, arr), Searcher.binarySearch(y, arr));
                    assertEquals(Searcher.linearSearch(x, arr), Searcher.binarySearch(x, arr));
                }
                trial++;
            }
        }
    }
}
