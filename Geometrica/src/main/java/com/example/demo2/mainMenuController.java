package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mainMenuController {


    @FXML
    private  void onAlgebraButtonClick(ActionEvent event) throws IOException {
        String sceneFile = "algebra-menu.fxml";

        sceneController.scenePicker(sceneFile, event);

    }
    @FXML
    public  void onGeometryButtonClick(MouseEvent event) throws IOException {

        if (event.getButton() == MouseButton.PRIMARY) {

            Graph graphController = sceneController.scenePicker("geometry-menu.fxml", event);

            ArrayList<RezPoint> points = new ArrayList<>(List.of(
                    new RezPoint(1, 10),
                    new RezPoint(20, 40),
                    new RezPoint(6, 9),
                    new RezPoint (32,12)
            ));
            ArrayList<RezPoint> points2 = new ArrayList<>(List.of(
                    new RezPoint(10, 20),
                    new RezPoint(2, 4),
                    new RezPoint(9, 4)

            ));

            graphController.addPoints(points,false,false,false);
            graphController.addPoints(points2,false,false,false);

        }
    }

    @FXML
    private void onCalculusButtonClick(MouseEvent event) throws IOException {

        if(event.getButton() == MouseButton.PRIMARY) {

            String sceneFile = "calculus-menu.fxml";

            sceneController.scenePicker(sceneFile, event);
        }
    }
}

