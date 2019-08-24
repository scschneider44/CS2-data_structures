package edu.caltech.cs2.lab02;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class LinearSearcherTests {

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
            int actual = Searcher.linearSearch(needle, new int[]{1, 3, 5});
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
    public class StudentWrittenLinearSearcherTests {

        public void testEmptyArray(int needle, int expected) {
            int actual = Searcher.linearSearch(needle, new int[]{});
            assertEquals(expected, actual);
        }

        public void testSingleEArray(int needle, int expected) {
            int actual = Searcher.linearSearch(needle, new int[]{5});
            assertEquals(expected, actual);
        }

        public void testEvenArray(int needle, int expected) {
            int actual = Searcher.linearSearch(needle, new int[]{1, 3, 5, 7});
            assertEquals(expected, actual);
        }

        public void testUnsortedArray(int needle, int expected) {
            int actual = Searcher.linearSearch(needle, new int[] {4, 1, 3});
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
