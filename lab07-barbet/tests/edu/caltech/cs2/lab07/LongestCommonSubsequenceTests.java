package edu.caltech.cs2.lab07;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LongestCommonSubsequenceTests {
    public void testLongestCommonSubsequence(String string1, String string2,
                                             int expected) {
        assertEquals(expected, LongestCommonSubsequence.findLCS(string1, string2));
    }

    @Order(0)
    @DisplayName("Longest Common Subsequence with Two Empty Strings")
    @Test
    public void testEmptyStrings() {
        testLongestCommonSubsequence("", "", 0);
    }

    @Order(1)
    @DisplayName("Longest Common Subsequence with One Empty String")
    @Test
    public void testOneEmptyString() {
        testLongestCommonSubsequence("", "abcdefghi", 0);
    }

    @Order(2)
    @DisplayName("Longest Common Subsequence with Indentical Strings")
    @Test
    public void testIndenticalStrings() {
        testLongestCommonSubsequence("abcdefghi", "abcdefghi", 9);
    }

    @Order(3)
    @DisplayName("Longest Common Subsequence with " +
            "First String as Subsequence of Second String")
    @Test
    public void testSubsequenceString() {
        testLongestCommonSubsequence("abcdefghi", "abcabcdefghiabc", 9);
    }

    @Order(4)
    @DisplayName("Longest Common Subsequence with One Possible Common Subsequence")
    @Test
    public void testOneCommonSubsequence() {
        testLongestCommonSubsequence("abcdefghi", "jklmnobpqrs", 1);
    }

    @Order(5)
    @DisplayName("Longest Common Subsequence with Non-Contiguous Subsequences")
    @Test
    public void testNonContiguousCommonSubsequence() {
        testLongestCommonSubsequence("abcdefghi", "abababefcghi", 7);
    }

    @Order(6)
    @DisplayName("Longest Common Subsequence Case Sensitivity")
    @Test
    public void testCaseSensitive() {
        testLongestCommonSubsequence("abcdefghi", "abababEFcghi", 6);
    }

    @Order(7)
    @DisplayName("Strings with Spaces")
    @Test
    public void testWithSpaces() {
        testLongestCommonSubsequence("hi hi", "hihi", 4);
        testLongestCommonSubsequence("hi hi", "hi hi", 5);
    }

    @Order(8)
    @DisplayName("All Characters in Subsequence Non-Contiguous")
    @Test
    public void testAllNonContiguous() {
        testLongestCommonSubsequence("axbxcxdx", "yaybychhhhh", 3);
    }

    @Order(9)
    @DisplayName("Large String 1")
    @Test
    public void testLarge1() {
        testLongestCommonSubsequence("abcdefghijklmnopqrstuvwxyz", "zyxwvutsrqponmlkjihgfedcba", 1);
    }

    @Order(10)
    @DisplayName("Large String 2")
    @Test
    public void testLarge2() {
        testLongestCommonSubsequence("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", 0);
    }

    @Order(10)
    @DisplayName("Large String 3")
    @Test
    public void testLarge3() {
        String a = "a".repeat(1000);
        String b = "b".repeat(1000);
        testLongestCommonSubsequence(a, b, 0);
    }

    @Order(11)
    @DisplayName("Large String 4")
    @Test
    public void testLarge4() {
        String a = "ba".repeat(1000);
        String b = "ab".repeat(1000);
        testLongestCommonSubsequence(a, b, 1999);
    }
}