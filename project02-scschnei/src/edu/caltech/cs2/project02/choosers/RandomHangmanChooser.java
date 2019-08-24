package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class RandomHangmanChooser implements IHangmanChooser {

  private int guessesRemaining;
  private final String secretWord;
  private SortedSet<Character> guesses;
  private static Random rand = new Random();




  public RandomHangmanChooser(int wordLength, int maxGuesses) { Scanner scan = null;
  try {
    scan = new Scanner(new File("data/scrabble.txt"));
  }
  catch (FileNotFoundException e) {
    System.out.println("File doesn't exist");
  }
    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException("word length and max guesses must be greater than one");
    }

    SortedSet<String> possibleWords = new TreeSet<>();

    while (scan.hasNext()) {
      String word = scan.nextLine();
      if (word.length() == wordLength) {
        possibleWords.add(word);
      }
    }


    if (possibleWords.size() == 0) {
      throw new IllegalStateException("No words of desired length");
    }

    List<String> wordList = new ArrayList<>(possibleWords);
    int randInd = rand.nextInt(possibleWords.size());

    this.guessesRemaining = maxGuesses;
    this.secretWord = wordList.get(randInd);
    this.guesses = new TreeSet<>();
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

    this.guessesRemaining -= 1;
    this.guesses.add(letter);
    int instances = 0;
    char[] letterArray = this.secretWord.toCharArray();
    for (char ch : letterArray) {
      if (ch == letter) {
        instances += 1;
      }
    }
    if (instances > 0) {
      this.guessesRemaining += 1;
    }
    return instances;
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
    String pattern = "";
    for (int i=0; i < this.getWord().length(); i++) {
      char letter = this.secretWord.charAt(i);
      if (this.getGuesses().contains(letter)) {
        pattern += letter;
      }
      else {
        pattern += "-";
      }
    }
    return pattern;
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
    return this.secretWord;
  }
}