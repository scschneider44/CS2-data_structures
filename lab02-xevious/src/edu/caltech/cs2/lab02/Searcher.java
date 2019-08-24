package edu.caltech.cs2.lab02;

public class Searcher {
    public static boolean isSorted(int[] array) {
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (prev > array[i]) {
                return false;
            }
            prev = array[i];
        }
        return true;
    }

    public static int linearSearch(int needle, int[] haystack) {
        if (!isSorted(haystack)) {
            throw new IllegalArgumentException("unsorted array");
        }
        for (int i = 0; i < haystack.length; i++)  {
            if (haystack[i] == needle) {
                return i;
            }
            if (haystack[i] > needle) {
                return -i - 1;
            }
        }
        return -haystack.length - 1;
    }

    public static int binarySearch(int needle, int[] haystack) {
        if (haystack.length > 0 && needle < haystack[0]) {
            return -1;
        }
        else if (haystack.length > 0 && needle == haystack[0]) {
            return 0;
        }
        int lowInd = 0;
        int highInd = haystack.length - 1;
        int midInd;
        while (lowInd <= highInd) {
            midInd = (highInd + lowInd) / 2;
            //System.out.printf("%d\n", midInd);
            if (needle < haystack[midInd] && haystack[midInd - 1] < needle) {
                  return -midInd - 1;
            }

            if (needle == haystack[midInd]) {
                return midInd;
            }

            if (needle > haystack[midInd]) {
                lowInd = midInd + 1;
            }

            if (needle < haystack[midInd]) {
                highInd = midInd - 1;
            }
        }
        return -haystack.length - 1;
    }
}
