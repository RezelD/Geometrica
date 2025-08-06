package com.example.demo2;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;

public class TextUpdater {

    private final Map<FormulaType, Formula> formulaMap = new HashMap<>();
    private final Set<FormulaType> activeFormulas = new HashSet<>();

    private final TextFlow textFlow;
    private final Graph graph;

    public TextUpdater(TextFlow textFlow, Graph graph) {

        this.textFlow = textFlow;

        textFlow.setTextAlignment( javafx.scene.text.TextAlignment.CENTER );
        this.graph = graph;

    }

    public void addFormula(FormulaType type) {

        Formula formula = FormulaMapper.mapFormula(type);
        formulaMap.put(type, formula);
        if (activeFormulas.contains(type)) {
            update();
        }
    }

    public void setActiveFormulas(Set<FormulaType> activeFormulas) {

        for  (FormulaType type : activeFormulas) {

            Formula formula = FormulaMapper.mapFormula(type);
            formulaMap.put(type, formula);
        }

        this.activeFormulas.clear();
        this.activeFormulas.addAll(activeFormulas);
        update();
    }

    public void update() {

        textFlow.getChildren().clear();

        if (activeFormulas.isEmpty()) {
            textFlow.getChildren().add(new Text("No active formulas"));
            return;
        }

        RezPolygon selected = graph.getSelectedPolygon();
        if (selected == null) {
            textFlow.getChildren().add(new Text("No polygon selected"));
            return;
        }


        FormulaParams polygonParam = new FormulaParams(selected);
        FormulaParams pointParam = new FormulaParams(null,null);
        ArrayList<RezPoint> measuredPoints = new ArrayList<>();

        if (selected.getPointCount() > 1) {
            measuredPoints.addAll(graph.getMeasuredPoints());
        }

        for (FormulaType type : activeFormulas) {

            Formula display = formulaMap.get(type);
            if (display != null) {

                if (type == FormulaType.DISTANCE) {

                    for (int i = 0; i < measuredPoints.size() - 1; i+=2) {

                        pointParam.point1 = measuredPoints.get(i);
                        pointParam.point2 = measuredPoints.get((i+1));
                        Object result = display.compute(new DistanceFormulaInput(pointParam.point1, pointParam.point2));

                        textFlow.getChildren().add(new Text(result + "\n"));
                    }
                }
            }
        }
    }
}