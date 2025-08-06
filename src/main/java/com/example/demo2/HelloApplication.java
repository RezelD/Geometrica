package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public Stage myStage;
    @Override
    public void start(Stage stage) throws IOException {

        this.myStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}