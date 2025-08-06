package com.example.demo2;

public class FormulaParams {

    public RezPolygon polygon;
    public RezPoint point1;
    public RezPoint point2;

    public FormulaParams(RezPolygon polygon) {
        this.polygon = polygon;
    }

    public FormulaParams(RezPoint point1, RezPoint point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
}
