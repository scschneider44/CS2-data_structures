package edu.caltech.cs2.libraries;

/***
 * This interface represents a gravitational body in an N-body simulation.
 * @param <B> the type of {@code IBody} to use when calculate forces
 */
public interface IBody<B extends IBody> {
  /***
   * Calculates and stores the gravitational force exerted by
   * all the bodies in {@code bodies} on this {@code IBody}.
   * If this {@code IBody} is included in {@code bodies},
   * it is omitted from the calculation, because bodies
   * don't exert gravitational forces on themselves.
   * @param bodies the bodies exerting gravitational forces on {@code this}
   */
  public void calculateNewForceFrom(B[] bodies);

  /***
   * Uses a previously calculated sum of pairwise gravitational
   * forces on this {@code IBody} to update its position after
   * {@code dt} time steps.
   * @param dt the number of time steps that have elapsed since
   *           the last position update
   */
  public void updatePosition(double dt);

  /***
   * Returns the current position of this {@code IBody}.
   * @return the current position of {@code this}
   */
  public Vector2D getCurrentPosition();

  /***
   * Returns the file name of the image that represents this {@code IBody}.
   * @return the image file name for {@code this}
   */
  public String getFileName();

  /***
   * Returns the string representation of this {@code IBody} which contains
   * all available information about this {@code IBody} in the same format
   * as the simulations.
   * @return string representation of {@code this} {@code IBody}
   */
  @Override
  public String toString();
}
