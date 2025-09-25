package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EnemiesManager {
    public static ArrayList<Enemies> enemies = new ArrayList<>();

    public static void CreateEnemies(AnchorPane scene) {
        TriangleC mob1 = new TriangleC(250, 0, 25, 25);
        enemies.add(mob1);
        mob1.getRectangle().setFill(Color.BLACK);
        scene.getChildren().add(mob1.getRectangle());
    }

    public static void updateEnemies(AnchorPane scene, double DeltaTime, ArrayList<Ball> balls,
                    ArrayList<Brick> bricks) {

        enemies.removeIf(e -> {
            if(e.update(DeltaTime, balls, bricks)) {
                scene.getChildren().remove(e.getShape());
                return true;
            }
            return false;
        });
    }
}
