package com.example.demo2;

public class DistanceFormula implements Formula<DistanceFormulaInput, Double> {

    @Override
    public Double compute(DistanceFormulaInput input) {

        double x1 = input.point1.getX();
        double y1 = input.point1.getY();
        double x2 = input.point2.getX();
        double y2 = input.point2.getY();

        double distance;

        distance = Math.abs(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
        return distance;
    }
}
