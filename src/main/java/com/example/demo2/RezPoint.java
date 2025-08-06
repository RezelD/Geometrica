package com.example.demo2;

import javafx.geometry.Point2D;

public class RezPoint {

    private double x;
    private double y;

    private boolean xMoveLocked = false;
    private boolean yMoveLocked = false;
    private boolean isMeasured = false;

    public RezPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public RezPoint(RezPoint point) {

        this.x = point.getX();
        this.y = point.getY();
        this.xMoveLocked = point.isXMoveLocked();
        this.yMoveLocked = point.isYMoveLocked();
        this.isMeasured = point.isMeasured();
    }

    public RezPoint(Point2D point) {
        this.x = point.getX();
        this.y = point.getY();
        this.xMoveLocked = false;
        this.yMoveLocked = false;
        this.isMeasured = false;
    }

    public RezPoint(double x, double y, boolean xMoveLocked, boolean yMoveLocked, boolean isMeasured) {

        this(x, y);
        this.xMoveLocked = xMoveLocked;
        this.yMoveLocked = yMoveLocked;
        this.isMeasured = isMeasured;
    }

    public void setMeasured(boolean measured) { this.isMeasured = measured; }
    public boolean isMeasured() { return isMeasured; }

    public double getX() { return x; }
    public double getY() { return y; }

    public void set(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }
    public void setX(double newX) { this.x = newX; }
    public void setY(double newY) { this.y = newY; }

    public void setXMoveLocked(boolean locked) { this.xMoveLocked = locked; }
    public void setYMoveLocked(boolean locked) { this.yMoveLocked = locked; }

    public boolean isXMoveLocked() { return xMoveLocked; }
    public boolean isYMoveLocked() { return yMoveLocked; }

    public RezPoint add(RezPoint point) { return new RezPoint(this.x + point.getX(), this.y + point.getY()); }
    public RezPoint add(double value) { return new RezPoint(this.x + value, this.y + value); }
    public RezPoint addX(double coordinateX) { return new RezPoint(this.x + coordinateX, this.y); }
    public RezPoint addY(double coordinateY) { return new RezPoint(this.x, this.y + coordinateY); }

    public RezPoint subtract(RezPoint point) { return new RezPoint(this.x - point.getX() , this.y - point.getY()); }
    public RezPoint subtract(double value) { return new RezPoint(this.x - value, this.y - value); }
    public RezPoint subtractX (double coordinateX) { return new RezPoint(this.x - coordinateX, this.y); }
    public RezPoint subtractY (double coordinateY) { return new RezPoint(this.x, this.y - coordinateY); }

    public RezPoint multiply(RezPoint point) { return new RezPoint(this.x * point.getX(), this.y * point.getY()); }
    public RezPoint multiply(double value) { return new RezPoint(this.x * value, this.y * value); }
    public RezPoint multiplyX(double coordinateX) { return new RezPoint(this.x * coordinateX, this.y); }
    public RezPoint multiplyY(double coordinateY) { return new RezPoint(this.x, this.y * coordinateY); }

    public RezPoint divide(RezPoint point) {
        if (point.getX() == 0 || point.getY() == 0) throw new IllegalArgumentException("RezPoint Divide: Cannot divide by zero");
        return new RezPoint(this.x / point.getX(), this.y / point.getY());
    }
    public RezPoint divide(double value) {
        if (value == 0) throw new IllegalArgumentException("Double Divide: Cannot divide by zero");
        return new RezPoint(this.x / value, this.y / value);
    }
    public RezPoint divideX(double coordinateX) {
        if (coordinateX == 0) throw new IllegalArgumentException("DivideX: Cannot divide by zero");
        return new RezPoint(this.x / coordinateX, this.y);
    }
    public RezPoint divideY(double coordinateY) {
        if (coordinateY == 0) throw new IllegalArgumentException("DivideY: Cannot divide by zero");
        return new RezPoint(this.x, this.y / coordinateY);
    }

    public static RezPoint convertToRezPoint(Point2D point) { return new RezPoint(point.getX(),point.getY()); }
}
