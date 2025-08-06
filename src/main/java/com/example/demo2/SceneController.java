package com.example.demo2;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.String;


public class SceneController {


    public static <T> T scenePicker(String sceneFile, Event event) throws IOException {

        double sceneHeight = ((Node) event.getSource()).getScene().getHeight();
        double sceneWidth = ((Node) event.getSource()).getScene().getWidth();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        return fxmlLoader.getController();
    }


}
