package com.example.demo2;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class RezPolygon {

    private final ArrayList<RezPoint> polygon;

    private boolean transformLocked = false;
    private boolean xMoveLocked = false;
    private boolean yMoveLocked = false;
    private boolean polygonPreselected = false;

    private int[] fillColor = new int[3];
    private int[] borderColor = new int[3];

    private double opacity;

    public RezPolygon(ArrayList<RezPoint> polygon) { this.polygon = polygon; }

    public boolean isInside(RezPolygon polygon, RezPoint mouseWorld) {

        ArrayList<RezPoint> points = polygon.getPolygon();

        if (points.size() < 3) return false;

        Path2D.Double path = new Path2D.Double();
        path.moveTo(points.getFirst().getX(),points.getFirst().getY());

        for (RezPoint point : points) {
            if (point.equals(points.getFirst())) continue;
            path.lineTo(point.getX(),point.getY());
        }
        path.closePath();

        return path.contains(mouseWorld.getX(),mouseWorld.getY());
    }


    public int[] getFillColor() { return fillColor; }
    public int[] getBorderColor() { return borderColor; }
    public double getOpacity() { return opacity; }

    public void setFillColor(int[] fillColor) { this.fillColor = fillColor; }
    public void setBorderColor(int[] borderColor) { this.borderColor = borderColor; }
    public void setOpacity(double opacity) { this.opacity = opacity; }



    public boolean isPolygonPreselected() { return polygonPreselected; }
    public void setPolygonPreselected(boolean polygonPreselected) { this.polygonPreselected = polygonPreselected; }
    public RezPoint getPoint(int index) { return polygon.get(index); }
    public int getPointIndex(RezPoint point) { return polygon.indexOf(point); }
    public int getPointCount() { return polygon.size(); }
    public boolean isTransformLocked() { return transformLocked; }
    public static boolean sharePoint(RezPolygon a, RezPolygon b) {
        for (RezPoint pointA : a.getPolygon()) {
            if (b.getPolygon().contains(pointA)) {
                return true;
            }
        }
        return false;
    }

    public void setTransformLocked(boolean locked) { this.transformLocked = locked; }

    public boolean isXMoveLocked() { return xMoveLocked; }
    public boolean isYMoveLocked() { return yMoveLocked; }

    public void setXMoveLocked(boolean locked) { this.xMoveLocked = locked; }
    public void setYMoveLocked(boolean locked) { this.yMoveLocked = locked; }

    public void clearPolygon() { polygon.clear(); }

    public void append(RezPoint point) { polygon.add(point); }

    public ArrayList<RezPoint> getPolygon() { return polygon; }

    public double[] toXArray() { return polygon.stream().mapToDouble(RezPoint::getX).toArray(); }
    public double[] toYArray() { return polygon.stream().mapToDouble(RezPoint::getY).toArray(); }
};
