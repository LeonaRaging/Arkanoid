package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EnemiesManager {
    public static ArrayList<Enemies> enemies = new ArrayList<>();

    public static void CreateEnemies(AnchorPane scene) {
        TriangleC mob1 = new TriangleC(36, 100, 15, 15);
        enemies.add(mob1);
        mob1.getRectangle().setFill(Color.BLACK);
        scene.getChildren().add(mob1.getRectangle());

        Mini_Saturn mob2 = new Mini_Saturn(72, 100, 7);
        enemies.add(mob2);
        mob2.getCircle().setFill(Color.GREEN);
        scene.getChildren().add(mob2.getCircle());

        Red_Blob mob3 = new Red_Blob(108, 100, 7);
        enemies.add(mob3);
        mob3.getCircle().setFill(Color.RED);
        scene.getChildren().add(mob3.getCircle());

        Bubble mob4 = new Bubble(36, 136, 15, 15); // 70 50 30 30
        enemies.add(mob4);
        mob4.getRectangle().setFill(Color.GRAY);
        scene.getChildren().add(mob4.getRectangle());
    }

    public static void updateEnemies(AnchorPane scene, double DeltaTime) {

        enemies.removeIf(e -> {
            if(e.update(DeltaTime, scene)) {
                scene.getChildren().remove(e.getShape());
                return true;
            }
            return false;
        });
    }
}
