package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {

  private int guessesRemaining;
  private SortedSet<String> possibleSecretWords;
  private SortedSet<Character> guesses;
  private String currPattern;

  public EvilHangmanChooser(int wordLength, int maxGuesses) { try {
    Scanner scan = new Scanner(new File("data/scrabble.txt"));
    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException("word length and max guesses must be greater than one");
    }

    this.possibleSecretWords = new TreeSet<>();

    while (scan.hasNext()) {
      String word = scan.nextLine();
      if (word.length() == wordLength) {
        this.possibleSecretWords.add(word);
      }
    }


    if (possibleSecretWords.size() == 0) {
      throw new IllegalStateException("No words of desired length");
    }

    String tempPattern = "";
    for (int i = 0; i < wordLength; i++) {
      tempPattern += "-";
    }

    this.guessesRemaining = maxGuesses;
    this.guesses = new TreeSet<>();
    this.currPattern = tempPattern;

  }
  catch (FileNotFoundException e) {
    e.printStackTrace();
  }
  }

  @Override
  public int makeGuess(char letter) {

    if (this.getGuessesRemaining() == 0) {
      throw new IllegalStateException("No more guesses left");
    }
    if (this.getGuesses().contains(letter)) {
      throw new IllegalArgumentException("Letter already guessed");
    }
    if (!Character.isLowerCase(letter)) {
      throw new IllegalArgumentException("Only lowercase letters accepted as guesses");
    }

    this.guesses.add(letter);
    Map<String, SortedSet<String>> patternMap = new TreeMap<>();

    for (String word : this.possibleSecretWords) {

      String pattern = "";

      for (int i=0; i < word.length(); i++) {

        char wordLetter = word.charAt(i);

        if (this.getGuesses().contains(wordLetter)) {
          pattern += wordLetter;
        }

        else {
          pattern += "-";
        }
      }

      if (!patternMap.containsKey(pattern)) {
        SortedSet<String> patternArray = new TreeSet<>();
        patternArray.add(word);
        patternMap.put(pattern, patternArray);
      }

      else {
        SortedSet<String> patternArray = patternMap.get(pattern);
        patternArray.add(word);
        patternMap.put(pattern, patternArray);
      }
    }

    int maxSize = 0;
    String patternSelect = "";
    for (Map.Entry<String, SortedSet<String>> entry : patternMap.entrySet()) {
      if (entry.getValue().size() > maxSize) {
        maxSize = entry.getValue().size();
        patternSelect = entry.getKey();
      }
    }

    int differences = 0;
    for (int i = 0; i < patternSelect.length(); i++) {
      if (patternSelect.charAt(i) != this.currPattern.charAt(i)) {
        differences += 1;
      }
    }

    this.possibleSecretWords = patternMap.get(patternSelect);
    this.currPattern = patternSelect;

    if (differences == 0) {
      this.guessesRemaining -= 1;
    }

    return differences;
  }

  @Override
  public boolean isGameOver() {
    if (!this.getPattern().contains("-") || this.getGuessesRemaining() == 0) {
      return true;
    }
    return false;
  }

  @Override
  public String getPattern() {
    return this.currPattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.guesses;
  }

  @Override
  public int getGuessesRemaining() {
    return this.guessesRemaining;
  }

  @Override
  public String getWord() {
    return this.possibleSecretWords.first();
  }
}