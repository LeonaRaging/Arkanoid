package com.arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        StackPane wrapper = new StackPane(root);
        Scene scene = new Scene(wrapper, 3 * 256, 3 * 224);
        stage.setTitle("Arkanoid");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        root.setScaleX(3);
        root.setScaleY(3);
        wrapper.setScaleShape(true);
    }

    public static void main(String[] args) {
        launch();
    }
}