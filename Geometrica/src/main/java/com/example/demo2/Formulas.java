package com.example.demo2;

public class Formulas {

    static double distance(RezPoint begin, RezPoint end) {

        double x1 = begin.getX();
        double y1 = begin.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        double distance;

        distance = Math.abs(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));


        return distance;

    }

}
