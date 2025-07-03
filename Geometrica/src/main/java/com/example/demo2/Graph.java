package com.example.demo2;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Graph {

    public Text pythagText;
    double zoomFactor = 100;
    RezPoint panOffset = new RezPoint(0,0);
    RezPoint lastMousePos = new RezPoint(0,0);
    ArrayList<RezPoint> points = new ArrayList<>();
    ArrayList<RezPolygon> polygons = new ArrayList<>();
    RezPolygon selectedPolygon;
    RezPoint selectedPoint;
    ArrayList<RezPoint> distance = new ArrayList<>();

    boolean draggingPoint = false;
    boolean movingPolygon = false;

    ArrayList<RezPoint> intersections = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("#.#####");


    @FXML
    private Canvas graph;

    @FXML
    void initialize() throws NonInvertibleTransformException {

        draw();
    }

    void clearPolygons() {

        polygons.clear();
        selectedPolygon = null;
        selectedPoint = null;
        draggingPoint = false;
        movingPolygon = false;
    }

    void addPoints(ArrayList<RezPoint> pointList, boolean transformLocked, boolean xMoveLocked, boolean yMoveLocked) {

        RezPolygon polygon = new RezPolygon(new ArrayList<>());


        if (pointList.size() > 1) {

            for (RezPoint point : pointList) {

                polygon.append(new RezPoint(point.getX(), point.getY()));




            }
            polygon.setXMoveLocked(xMoveLocked);
            polygon.setYMoveLocked(yMoveLocked);
            polygon.setTransformLocked(transformLocked);

            polygons.add(polygon);
        } else {

            points.addAll(pointList);
        }
    }

    Affine worldToScreen() {

        Affine worldToScreen = new Affine();
        worldToScreen.appendTranslation(
                graph.getWidth() / 2.0 + panOffset.getX(),
                graph.getHeight() / 2.0 + panOffset.getY());
        worldToScreen.appendScale(zoomFactor, -zoomFactor);

        return worldToScreen;

    }

    void draw() throws NonInvertibleTransformException {

        intersections.clear();

        GraphicsContext gc = graph.getGraphicsContext2D();


        gc.setLineWidth(1.0 / zoomFactor);     // counteracts zoom




        gc.setTransform(worldToScreen());




        final Rectangle2D doubleRect = new Rectangle2D(0,0,graph.getWidth(),graph.getHeight());

        Affine screenToWorld = worldToScreen().createInverse();

        Point2D topLeft = screenToWorld.transform(doubleRect.getMinX(), doubleRect.getMinY());
        Point2D bottomRight = screenToWorld.transform(doubleRect.getMaxX(),doubleRect.getMaxY());

        final Rectangle2D worldRect = new Rectangle2D (

                Math.min(topLeft.getX(), bottomRight.getX()),
                Math.min(topLeft.getY(),bottomRight.getY()),
                abs(bottomRight.getX() - topLeft.getX()),
                abs(bottomRight.getY() - topLeft.getY())
        );

        gc.clearRect(worldRect.getMinX(),worldRect.getMinY(),worldRect.getWidth(),worldRect.getHeight());

        final double step = gridStep();
        final int firstX = (int) ((worldRect.getMinX() / step) - 1);
        final int lastX  = (int) ((worldRect.getMaxX() / step) + 1);
        final int firstY = (int) ((worldRect.getMinY() / step) - 1);
        final int lastY  = (int) ((worldRect.getMaxY() / step) + 1);

        for (int i = firstX; i <= lastX; i++) {

            if ( i == 0) {
                continue;
            }
            final double x = i * step;

            gc.setStroke(Color.DARKGRAY);
            gc.strokeLine(x,worldRect.getMinY(),x,worldRect.getMaxY());
        }

        for (long k = firstX * 5L; k<= lastX * 5L; k++) {


            gc.setStroke(Color.LIGHTGRAY);

            final double x = k * step;

            double minorX = x/5;

        ;

            for (long l = firstY * 5L; l<= lastY * 5L; l++) {


                final double y = l * step;

                double minorY = y/5;


                RezPoint intersection = new RezPoint(minorX,minorY);

                intersections.add(intersection);

                if (l%5==0) continue;
                if (k%5==0) continue;




                gc.strokeLine(worldRect.getMinX(),minorY,worldRect.getMaxX(),minorY);
            }
            if (k%5==0) continue;

            gc.strokeLine(minorX, worldRect.getMinY(),minorX,worldRect.getMaxY());


        }






        for (long j = firstY; j <= lastY; j++) {

            if ( j == 0) {
                continue;
            }

            gc.setStroke(Color.DARKGRAY);

            final double y = j * step;

            gc.strokeLine(worldRect.getMinX(),y,worldRect.getMaxX(),y);

        }

        gc.setStroke(Color.BLACK);

        gc.strokeLine(worldRect.getMinX(),0,worldRect.getMaxX(),0);

        gc.strokeLine(0,worldRect.getMinY(),0,worldRect.getMaxY());



        if(!polygons.isEmpty()) {

            int i = 0;

            for (final RezPolygon polygon : polygons) {

                Color fillColor = Color.rgb(polygon.getFillColor()[0],polygon.getFillColor()[1],polygon.getFillColor()[2],polygon.getOpacity());
                Color borderColor = Color.rgb(polygon.getBorderColor()[0],polygon.getBorderColor()[1],polygon.getBorderColor()[2],1);


                switch (i) {
                    case 0 -> {
                        polygon.setFillColor(new int[]{60, 120, 240});
                        polygon.setOpacity(0.4);
                        polygon.setBorderColor(new int[]{0, 0, 255});
                    }
                    case 1 -> {
                        polygon.setFillColor(new int[]{60, 240, 120});
                        polygon.setOpacity(0.4);
                        polygon.setBorderColor(new int[]{40, 100, 40});
                    }
                    case 2 -> {
                        polygon.setFillColor(new int[]{240, 60, 120});
                        polygon.setOpacity(0.4);
                        polygon.setBorderColor(new int[]{255, 0, 0});
                    }
                    default -> {
                        fillColor = Color.rgb(240, 240, 60, .40);
                        polygon.setFillColor(new int[]{240, 240, 60});
                        polygon.setOpacity(0.4);
                        polygon.setBorderColor(new int[]{255, 255, 0});
                    }
                };
                gc.setStroke(borderColor);
                gc.setFill(fillColor);

                double[] xPoints = polygon.toXArray();
                double[] yPoints = polygon.toYArray();




                gc.fillPolygon(xPoints,yPoints,xPoints.length);
                gc.strokePolygon(xPoints,yPoints,xPoints.length);

                i++;
            }
        }

        for (RezPoint intersection : intersections) {

            if (false) { //test statement

                gc.save();
                gc.setTransform(new Affine());
                final int radiusX = 2;
                final int radiusY = 2;



                gc.setStroke(Color.RED);
                gc.setFill(Color.RED);

                Point2D trueIntersection = worldToScreen().transform(intersection.getX(), intersection.getY());


                gc.fillOval(trueIntersection.getX(),trueIntersection.getY()-radiusY,radiusX,radiusY);


                gc.restore();
            }

        }


        for (int i = firstX; i <= lastX; i++) {

            final double x = i * step;
            final double toPixelX = (graph.getWidth() * 0.5 + panOffset.getX() + x * zoomFactor);
            double xTextYValue = panOffset.getY() + graph.getHeight() * 0.55;

            gc.save();
            gc.setTransform(new Affine());

            gc.setFill(Color.BLACK);

            if (xTextYValue < 20) {

                xTextYValue = 20;
            }
            else if (xTextYValue > 400) {        // keep y-axis labels on screen when panning

                xTextYValue = 400;
            }

            if ((x < 0.00001 && x > -.00001) || x > 999999 || x < -999999) {

                if (x == 0) {
                    continue;
                }
                df.applyPattern("0.#####E0");

            } else {
                df.applyPattern("#.#####");
            }

            final String xText = df.format(x);


            gc.fillText(xText,toPixelX,xTextYValue);

            gc.restore();
        }
        for (long j = firstY; j <= lastY; j++) {


            final double y = j * step;
            final double toPixelY = (graph.getHeight() * 0.5 + panOffset.getY() - y * zoomFactor);
            double yTextXValue = panOffset.getX()+205 ;

            gc.save();
            gc.setTransform(new Affine());

            gc.setFill(Color.BLACK);


            if (yTextXValue < 0) {

                yTextXValue = 0;
            }
            else if (yTextXValue > 350) {

                // keep y-axis labels on screen when panning

                yTextXValue = 350;
            }

            if ((y < 0.00001 && y > -.00001) || y > 999999 || y < -999999) {

                if (y == 0) continue;
                df.applyPattern("0.#####E0");


            } else {
                df.applyPattern("#.#####");
            }

            final String yText = df.format(y);;


            gc.fillText(yText,yTextXValue,toPixelY);

            gc.restore();

        }



    }


    double gridStep()
    {

        double pxStep = 100;
        double mult;

        final double worldSpacing = pxStep / zoomFactor;
        final double powerOf10 = Math.pow(10, Math.floor(Math.log10(worldSpacing)));


        if (worldSpacing < 2.0 * powerOf10) {

            mult = 1;
        }
        else if (worldSpacing < 5.0 * powerOf10) {

            mult = 2;
        }
        else {

            mult = 5;
        }

        return powerOf10 * mult;
    }
    @FXML
    void zoom(ScrollEvent event) throws NonInvertibleTransformException {

        final double zoomStep = 1.15;
        final double scale = (event.getDeltaY() > 0) ? zoomStep : 1.0 / zoomStep;
        lastMousePos.set(event.getX() , event.getY());
        RezPoint center = new RezPoint(graph.getWidth() / 2.0, graph.getHeight() / 2.0);


        final RezPoint mouseBeforeZoomPosition = (lastMousePos.subtract(center).subtract(panOffset)).divide(zoomFactor);

        zoomFactor *= scale;

        final RezPoint mouseAfterZoomPosition = (lastMousePos.subtract(center).subtract(panOffset)).divide(zoomFactor);

        panOffset = panOffset.add(mouseAfterZoomPosition.subtract(mouseBeforeZoomPosition).multiply(zoomFactor));

        intersections.clear();

        draw();
        event.consume();


    }


    @FXML
    void press(MouseEvent event) throws NonInvertibleTransformException {
        if (event.getButton() != MouseButton.PRIMARY) return;
        lastMousePos.set(event.getX(), event.getY());

        RezPoint mouseWorld = RezPoint.convertToRezPoint(worldToScreen().createInverse().transform(event.getX(), event.getY()));

        double grabDistance = gridStep() / 5;

        if (selectedPolygon != null) {
            for (RezPoint point : selectedPolygon.getPolygon()) {

                if (event.getClickCount() == 2) {
                    distance.add(point.subtract(mouseWorld));


                }


                if (Formulas.distance(mouseWorld, point) <= grabDistance) {
                    selectedPoint = point;
                    draggingPoint = true;
                    draw();
                    return;
                }
            }
        }

        double minDist = Double.MAX_VALUE;

        RezPoint closestPoint = null;
        RezPolygon owningPolygon = null;
        for (RezPolygon polygon : polygons) {
            for (RezPoint point : polygon.getPolygon()) {
                double dist = Formulas.distance(mouseWorld, point);
                if (dist <= grabDistance && dist < minDist) {
                   closestPoint = point;
                   owningPolygon = polygon;
                    minDist = dist;
                }
            }
        }

        if (closestPoint != null) {
            selectedPoint = closestPoint;
            selectedPolygon = owningPolygon;
            draggingPoint = true;

            draw();
            return;
        }

        for (RezPolygon polygon : polygons) {
            if (polygon.isInside(polygon, mouseWorld)) {
                polygon.setOpacity(0.9);


                    selectedPolygon = polygon;

                    selectedPoint = null;
                    draggingPoint = false;
                    draw();
                    return;



            }
        }

        selectedPolygon = null;
        selectedPoint = null;
        draggingPoint = false;

        draw();
    }





    @FXML
    void move(MouseEvent event) throws NonInvertibleTransformException {

        System.out.println(selectedPolygon);
        RezPoint mouseMovement = new RezPoint(
                event.getX() - lastMousePos.getX(),
                event.getY() - lastMousePos.getY()
        );


        RezPoint mouseWorldLocation = RezPoint.convertToRezPoint(
                worldToScreen().createInverse().transform(event.getX(), event.getY())
        );

        for (RezPolygon polygon : polygons) {

            if (polygon.isInside(polygon,mouseWorldLocation)) {

                polygon.setOpacity(0.7);


            }

        }
        System.out.println(mouseWorldLocation);
        draw();




        if (event.getButton() == MouseButton.PRIMARY) {



            RezPoint closestIntersection = null;
            double minDistance = Double.MAX_VALUE;

            for (RezPoint intersection : intersections) {
                double distance = Formulas.distance(mouseWorldLocation, intersection);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestIntersection = intersection;
                }
            }

            if (closestIntersection == null) closestIntersection = mouseWorldLocation;

            if (!draggingPoint && selectedPoint == null && selectedPolygon != null && !distance.isEmpty()) {
                int count = 0;
                for (RezPoint point : selectedPolygon.getPolygon()) {


                    selectedPolygon.getPolygon().set(count, mouseWorldLocation.add(distance.get(count)));
                    System.out.print("X: %f Y: %f\n");

                    count++;
                }
                mouseMovement.set(0, 0);

                draw();
            }
            if (selectedPolygon != null && draggingPoint && selectedPoint != null) {
                selectedPoint.set(closestIntersection.getX(), closestIntersection.getY());
                draw();

            }

            if (!draggingPoint && selectedPolygon == null && selectedPoint == null) {

                intersections.clear();


                panOffset = panOffset.add(mouseMovement);
                lastMousePos.set(event.getX(), event.getY());
                draw();
            }
        }
    }



    @FXML
    void release(MouseEvent event) throws NonInvertibleTransformException {

        if (event.getButton() == MouseButton.PRIMARY && event.getEventType() == MouseEvent.MOUSE_RELEASED) {

            draggingPoint = false;
            distance.clear();

            for (RezPolygon polygon : polygons) {

                if (polygon.isInside(polygon, RezPoint.convertToRezPoint(worldToScreen().createInverse().transform(event.getX(), event.getY())))) {
                    polygon.setOpacity(0.7);
                }
            }
            draw();
        }
    }



}

class RezPoint {

    private double x;
    private double y;

    private boolean xMoveLocked = false;
    private boolean yMoveLocked = false;

    public RezPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

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

class RezPolygon {

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


