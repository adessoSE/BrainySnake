package de.adesso.brainysnake.playercommon.math;

public class Point2D {

    public int x;
    public int y;

    /** Constructs a new 2D grid point. */
    public Point2D () {
    }

    /** Constructs a new 2D grid point.
     *
     * @param x X coordinate
     * @param y Y coordinate */
    public Point2D (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Copy constructor
     *
     * @param point The 2D grid point to make a copy of. */
    public Point2D (Point2D point) {
        this.x = point.x;
        this.y = point.y;
    }

    /** Sets the coordinates of this 2D grid point to that of another.
     *
     * @param point The 2D grid point to copy the coordinates of.
     *
     * @return this 2D grid point for chaining. */
    public Point2D set (Point2D point) {
        this.x = point.x;
        this.y = point.y;
        return this;
    }

    /** Sets the coordinates of this 2D grid point.
     *
     * @param x X coordinate
     * @param y Y coordinate
     *
     * @return this 2D grid point for chaining. */
    public Point2D set (int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * @param other The other point
     * @return the squared distance between this point and the other point.
     */
    public float dst2 (Point2D other) {
        int xd = other.x - x;
        int yd = other.y - y;

        return xd * xd + yd * yd;
    }

    /**
     * @param x The x-coordinate of the other point
     * @param y The y-coordinate of the other point
     * @return the squared distance between this point and the other point.
     */
    public float dst2 (int x, int y) {
        int xd = x - this.x;
        int yd = y - this.y;

        return xd * xd + yd * yd;
    }

    /**
     * @param other The other point
     * @return the distance between this point and the other vector.
     */
    public float dst (Point2D other) {
        int xd = other.x - x;
        int yd = other.y - y;

        return (float)Math.sqrt(xd * xd + yd * yd);
    }

    /**
     * @param x The x-coordinate of the other point
     * @param y The y-coordinate of the other point
     * @return the distance between this point and the other point.
     */
    public float dst (int x, int y) {
        int xd = x - this.x;
        int yd = y - this.y;

        return (float)Math.sqrt(xd * xd + yd * yd);
    }

    /**
     * Adds another 2D grid point to this point.
     *
     * @param other The other point
     * @return this 2d grid point for chaining.
     */
    public Point2D add (Point2D other) {
        x += other.x;
        y += other.y;
        return this;
    }

    /**
     * Adds another 2D grid point to this point.
     *
     * @param x The x-coordinate of the other point
     * @param y The y-coordinate of the other point
     * @return this 2d grid point for chaining.
     */
    public Point2D add (int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Subtracts another 2D grid point from this point.
     *
     * @param other The other point
     * @return this 2d grid point for chaining.
     */
    public Point2D sub (Point2D other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    /**
     * Subtracts another 2D grid point from this point.
     *
     * @param x The x-coordinate of the other point
     * @param y The y-coordinate of the other point
     * @return this 2d grid point for chaining.
     */
    public Point2D sub (int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * @return a copy of this grid point
     */
    public Point2D cpy () {
        return new Point2D(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2D)) return false;

        Point2D point2D = (Point2D) o;

        if (x != point2D.x) return false;
        return y == point2D.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
