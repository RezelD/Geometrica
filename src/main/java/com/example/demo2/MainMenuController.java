package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MainMenuController {

    TextUpdater textUpdater;

    @FXML
    private  void onAlgebraButtonClick(ActionEvent event) throws IOException {
        String sceneFile = "algebra-menu.fxml";

        SceneController.scenePicker(sceneFile, event);

    }

    @FXML
    public  void onGeometryButtonClick(MouseEvent event) throws IOException {

        if (event.getButton() == MouseButton.PRIMARY) {

            Graph graphController = SceneController.scenePicker("geometry-menu.fxml", event);

            ArrayList<RezPoint> points = new ArrayList<>(List.of(
                    new RezPoint(1, 10,false,false,true),
                    new RezPoint(20, 40,false,false,true),
                    new RezPoint(6, 9,false,false,false),
                    new RezPoint (32,12,false,false,false)
            ));
            ArrayList<RezPoint> points2 = new ArrayList<>(List.of(
                    new RezPoint(10, 20,true,false,false),
                    new RezPoint(2, 4,false,true,true),
                    new RezPoint(9, 4,true,true,true)

            ));

            TextFlow pythagText = graphController.getTextFlow();
            textUpdater = new TextUpdater(pythagText, graphController);
            textUpdater.setActiveFormulas(new HashSet<>(List.of(FormulaType.DISTANCE)));
            graphController.setTextUpdater(textUpdater);

            graphController.addPoints(points,false,false,false);
            graphController.addPoints(points2,false,false,false);

        }
    }

    @FXML
    private void onCalculusButtonClick(MouseEvent event) throws IOException {

        if(event.getButton() == MouseButton.PRIMARY) {

            String sceneFile = "calculus-menu.fxml";

            SceneController.scenePicker(sceneFile, event);
        }
    }
}

