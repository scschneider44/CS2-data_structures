package edu.caltech.cs2.libraries;

/***
 * This class represents a two-dimensional vector of real numbers.
 */
public class Vector2D {
  private static final double EQUALITY_THRESHOLD = 0.0000000000001;
  private double x;
  private double y;

  /***
   * Constructs a new {@code Vector2D} from its x and y components.
   * @param x the x component of the vector
   * @param y the y component of the vector
   */
  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /***
   * Returns the x component of this vector
   * @return the x component of {@code this}
   */
  public double getX() {
    return this.x;
  }

  /***
   * Returns the y component of this vector
   * @return the y component of {@code this}
   */
  public double getY() {
    return this.y;
  }

  /***
   * Returns a {@code Vector2D} whose value is ({@code this} + {@code other})
   * @param other value to be added to {@code this} {@code Vector2D}
   * @return {@code this} + {@code other})
   */
  public Vector2D add(Vector2D other) {
    return new Vector2D(this.getX() + other.getX(), this.getY() + other.getY());
  }
  
  /***
   * Returns a {@code Vector2D} whose value is (s * {@code this}), where s is
   * some scalar.
   * @param s scalar value to multiply {@code this} {@code Vector2D} by
   * @return s * {@code this}
   */
  public Vector2D scale(double s) {
    return new Vector2D(this.getX() * s, this.getY() * s);
  }

  @Override
  public String toString() {
    return "<" + this.getX() + " " + this.getY() + ">";
  }

  /***
   * Returns a new {@code Vector2D} constructed from the string representation
   * described in {@code toString()}.
   * @param vector the string representation of the {@code Vector2D} to be constructed
   * @return a {@code Vector2D} represented by the string {@code vector}
   */
  public static Vector2D fromString(String vector){
    String[] pieces = vector.substring(1, vector.length() - 1).split("\\s+");
    return new Vector2D(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]));
  }

  private static boolean doubleCompare(double a, double b) {
    return Double.compare(a, b) == 0 || Math.abs((a - b)/a) < Vector2D.EQUALITY_THRESHOLD;
  }

  /***
   * Compares this {@code Vector2D} with the specified {@code Object} for equality
   * @param other Object to which this {@code Vector2D} is to be compared
   * @return {@code true} if and only if the specified {@code Object} is
   *          a {@code Vector2D} with the same x and y components as {@code this}
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Vector2D)) {
      return false;
    }

    Vector2D otherV = (Vector2D)other;
    return doubleCompare(this.x, otherV.x) && doubleCompare(this.y, otherV.y);
  }
}