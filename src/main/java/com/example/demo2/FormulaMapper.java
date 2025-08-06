package com.example.demo2;

public class FormulaMapper {

    public static Formula mapFormula(FormulaType formulaType){

        if (formulaType == FormulaType.DISTANCE) {

            return new DistanceFormula();
        }

        return null;
    }
}
