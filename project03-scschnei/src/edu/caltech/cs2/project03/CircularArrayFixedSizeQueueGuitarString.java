package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {

  private static final int SAMPLING_RATE = 44100;
  private static final double ENERGY_DECAY_FACTOR = 0.996;
  private static Random rand = new Random();
  private IFixedSizeQueue<Double> guitarString;

  public CircularArrayFixedSizeQueueGuitarString(double frequency) {
    this.guitarString = new CircularArrayFixedSizeQueue<>((int) (SAMPLING_RATE / frequency) + 1);
    for (int i=0; i < (int) (SAMPLING_RATE / frequency) + 1; i++) {
      this.guitarString.enqueue(0.0);
    }
  }

  public int length() {
      return this.guitarString.capacity();
  }

  public void pluck() {
    for (int i=0; i < this.length(); i++) {
      this.guitarString.dequeue();
      System.out.print(rand.nextDouble() - 0.5);
      this.guitarString.enqueue(rand.nextDouble() - 0.5);
    }
  }

  public void tic() {
    double removed = this.guitarString.dequeue();
    double head = this.guitarString.peek();
    double add = ((removed + head) / 2) * ENERGY_DECAY_FACTOR;
    this.guitarString.enqueue(add);
  }

  public double sample() {
      return this.guitarString.peek();
  }
}
