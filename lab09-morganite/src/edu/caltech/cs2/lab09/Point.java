package edu.caltech.cs2.lab09;

public class Point {
    public int x; // x coordinate of point
    public int y; // y coordinate of point
    public Point parent; // Parent node of point (may be null)

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
    }

    /*
     * Returns true if the point passed has the same x and y coordinate as
     * this point, or false otherwise.
     */
    public boolean isEqual(Point point) {
        if (point == null) return false;
        int x = point.x;
        int y = point.y;
        return (this.x == x && this.y == y);
    }

    /*
     * Returns true if the point passed has the same x and y coordinate as
     * this point, or false otherwise.
     */
    @Override
    public boolean equals(Object point) {
        if (!(point instanceof Point)) {
            return false;
        }
        Point p = (Point) point;
        int x = p.x;
        int y = p.y;
        return (this.x == x && this.y == y);
    }
}
