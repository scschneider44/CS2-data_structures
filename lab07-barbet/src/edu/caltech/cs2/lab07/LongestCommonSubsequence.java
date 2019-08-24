package edu.caltech.cs2.lab07;

public class LongestCommonSubsequence {
    public static int findLCS(String string1, String string2) {
        int[][] memo = new int[string1.length()+1][string2.length()+1];
        for (int i=0; i <= string1.length(); i++) {
            for (int j=0; j <= string2.length(); j++) {
                memo[i][j] = -1;
            }
        }
        return findLCS(string1, string2, memo);
    }
    private static int findLCS(String string1, String string2, int[][] memo) {
        if (string1.length() == 0) {
            memo[0][string2.length()] = 0;
            return 0;
        }
        if (string2.length() == 0) {
            memo[string1.length()][0] = 0;
            return 0;
        }
        int sub = memo[string1.length()][string2.length()];
        if (sub != -1) {
            return sub;
        }
        String newS1 = string1.substring(0, string1.length()-1);
        String newS2 = string2.substring(0, string2.length()-1);
        if (string1.charAt(string1.length()-1) == string2.charAt(string2.length() -1)) {
            memo[string1.length()][string2.length()] = findLCS(newS1, newS2, memo) + 1;
            return memo[string1.length()][string2.length()];
        }
        else {
            int chop2 = findLCS(string1, newS2, memo);
            int chop1 = findLCS(newS1, string2, memo);
            int best = Math.max(chop1, chop2);
            memo[string1.length()][newS2.length()] = chop2;
            memo[newS1.length()][string2.length()] = chop1;
            return best;
        }
    }
}