package edu.caltech.cs2.libraries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/***
 * A faked implementation of the {@code IBody} interface. This implementation
 * used pre-calculated values specifically corresponding to the {@code planets.txt}
 * simulation.
 */
public class FakeBody implements IBody<FakeBody> {
  private String bodyname;
  private Scanner in;

  public FakeBody(double xPos, double yPos, double xVel, double yVel, double mass, String filename) {
    this.bodyname = filename.split("\\.")[0];

    // The fake implementation looks up the value in a file and reads the file as the simulation unfold
    String tracename = "data/fake/values-" + this.bodyname + ".txt";

    try {
      this.in = new Scanner(new File(tracename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(tracename + " is not a valid trace file.");
    }
  }

  @Override
  public void calculateNewForceFrom(FakeBody[] bodies) {
    // The fake implementation is to do nothing
  }

  @Override
  public void updatePosition(double dt) {
    // The fake implementation is to do nothing
  }

  @Override
  public Vector2D getCurrentPosition() {
    return Vector2D.fromString(in.nextLine());
  }

  @Override
  public String getFileName() {
    return this.bodyname + ".gif";
  }

  /***
   * Returns a string representation of this {@code FakeBody} closely corresponding to the
   * simulation format.  Notably, however, since {@code FakeBody} does not keep track of velocity,
   * the x and y components of the velocity will <b>not</b> be included in the return value.
   * @return a string representation of {@code this} {@code FakeBody}
   */
  @Override
  public String toString() {
    Vector2D position = this.getCurrentPosition();
    return String.format("%11.4e %11.4e %12s", position.getX(), position.getY(), this.getFileName());
  }
}