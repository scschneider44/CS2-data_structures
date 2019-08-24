package edu.caltech.cs2.project01;

import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.Vector2D;

public class Body implements IBody<Body> {
  private static final double G = 6.67 * Math.pow(10, -11);
  private Vector2D position;
  private Vector2D velocity;
  private Vector2D force;
  private final double mass;
  private final String filename;

  public Body(Vector2D position, Vector2D velocity, double mass, String filename) {
    this(position.getX(), position.getY(), velocity.getX(), velocity.getY(), mass, filename);
  }
  public Body(double xPos, double yPos, double xVel, double yVel, double mass, String filename) {
    Vector2D position = new Vector2D(xPos, yPos);
    Vector2D velocity = new Vector2D(xVel, yVel);
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.filename = filename;
    this.force = new Vector2D(0, 0);
  }
  public Body(double xPos, double yPos, double xVel, double yVel) {
    this(xPos, yPos, xVel, yVel, 0, null);
  }


  @Override
  public void calculateNewForceFrom(Body[] bodies) {
    // get all the forces from the other bodies on this body
    double totalXForce = 0;
    double totalYForce = 0;
    for (Body body : bodies) {
      if (this != body) {
        totalXForce += this.getForceFrom(body).getX();
        totalYForce += this.getForceFrom(body).getY();
      }
    }
    this.force = new Vector2D(totalXForce, totalYForce);
  }

  @Override
  public void updatePosition(double dt) {
    // update position using physics
    double xAccel = this.force.getX() / this.mass;
    double yAccel = this.force.getY() / this.mass;
    double newXVel = this.velocity.getX() + xAccel * dt;
    double newYVel = this.velocity.getY() + yAccel * dt;
    double newXPos = this.position.getX() + newXVel * dt;
    double newYPos = this.position.getY() + newYVel * dt;

    this.velocity = new Vector2D(newXVel, newYVel);
    this.position = new Vector2D(newXPos, newYPos);
  }

  @Override
  public Vector2D getCurrentPosition() {
    return this.position;
  }

  @Override
  public String getFileName() {
    return this.filename;
  }

  @Override
  public String toString() {
    Vector2D position = this.getCurrentPosition();
    return String.format("%11.4e %11.4e %11.4e %11.4e %11.4e %12s", position.getX(),
            position.getY(), this.velocity.getX(), this.velocity.getY(), this.mass, this.getFileName());
  }

  private double distanceTo(Body other) {
    // euclidean distance
    Vector2D body1 = this.getCurrentPosition();
    Vector2D body2 = other.getCurrentPosition();
    return (Math.sqrt(Math.pow(body2.getX() - body1.getX(), 2) + Math.pow(body2.getY() - body1.getY(), 2)));
  }

  private Vector2D getForceFrom(Body other) {
    // Get force between bodies using physics
    double mass1 = this.mass;
    double mass2 = other.mass;
    double dist = this.distanceTo(other);
    double totalForce = (G * mass1 * mass2) / (dist * dist);
    double deltaX = other.getCurrentPosition().getX() - this.getCurrentPosition().getX() ;
    double deltaY = other.getCurrentPosition().getY() - this.getCurrentPosition().getY();
    double xForce = totalForce * (deltaX / dist);
    double yForce = totalForce * (deltaY / dist);
    return new Vector2D(xForce, yForce);
  }
}