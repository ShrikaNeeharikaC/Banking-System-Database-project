package com.minibanking.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // This line loads the FXML file from the resources folder.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));

        // Set the title of the window.
        primaryStage.setTitle("Mini Banking System");

        // Create a new scene with the loaded layout and set its size.
        primaryStage.setScene(new Scene(root, 800, 600));

        // Show the window to the user.
        primaryStage.show();
    }

    public static void main(String[] args) {
        // This is the main method that launches the JavaFX application.
        launch(args);
    }
}

