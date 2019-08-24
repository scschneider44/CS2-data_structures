package edu.caltech.cs2.lab06;

import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary) {
        LetterBag phraseBag = new LetterBag(phrase);
        List<String> accumulator = new ArrayList<>();
        printPhrases(phraseBag, dictionary, accumulator);
        if (phrase.equals("") || phrase.equals(" ")) {
            System.out.println("[]");
        }
    }
    private static void printPhrases(LetterBag phrase, List<String> dictionary, List<String> accumulator) {
        for (String word : dictionary) {
            LetterBag wordBag = new LetterBag(word);
            LetterBag remainder = phrase.subtract(wordBag);
            if (remainder != null) {
                accumulator.add(word);
                if (remainder.isEmpty()) {
                    System.out.println(accumulator.toString());
                }
                else {
                    printPhrases(remainder, dictionary, accumulator);
                }
                accumulator.remove(word);
            }
        }
    }

    public static void printWords(String word, List<String> dictionary) {
        for (String dictWord : dictionary) {
            LetterBag wordBag = new LetterBag(word);
            LetterBag dictBag = new LetterBag((dictWord));
            LetterBag remainder = dictBag.subtract(wordBag);
            if (remainder != null) {
                if (remainder.isEmpty()) {
                    System.out.println(dictWord);
                }
            }
        }
    }
}
