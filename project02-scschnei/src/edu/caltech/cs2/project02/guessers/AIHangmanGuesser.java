package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static String dictionary = "data/scrabble.txt";

  @Override
  public char getGuess(String pattern, Set<Character> guesses) {

    Map<Character, Integer> numChars = new TreeMap<>();
    List<String> possibleWords = new ArrayList<>();
    Scanner s = null;
    try {
      s = new Scanner(new File(dictionary));
    }
    catch (FileNotFoundException e){
      System.out.print("No such file found");
    }
    while (s.hasNext()) {
      String word = s.next();

      if (word.length() == pattern.length()) {

        String wordPattern = "";
        for (int i = 0; i < word.length(); i++) {

          char wordLetter = word.charAt(i);

          if (guesses.contains(wordLetter)) {
            wordPattern += wordLetter;
          } else {
            wordPattern += "-";
          }
        }

        if (pattern.equals(wordPattern)) {
          possibleWords.add(word);
        }
      }
    }

      for (String wrd : possibleWords) {
        char[] letters = wrd.toCharArray();
        for (char ch : letters) {
          if (!guesses.contains(ch)) {
            if (!numChars.keySet().contains(ch)) {
              numChars.put(ch, 1);
            } else {
              numChars.put(ch, numChars.get(ch) + 1);
            }
          }
        }
      }


    int mostChar = 0;
    char bestChar = '\0';
    for (Map.Entry<Character, Integer> entry : numChars.entrySet()) {
      if (entry.getValue() > mostChar) {
        mostChar = entry.getValue();
        bestChar = entry.getKey();
      }
    }
    return bestChar;
  }
}
