package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EnemiesManager {
    public static ArrayList<Enemies> enemies = new ArrayList<>();

    public static void CreateEnemies(AnchorPane scene) {
        Mini_Saturn mob1 = new Mini_Saturn(244, 0, 25, 25);
        enemies.add(mob1);
        mob1.getPos().setFill(Color.BLACK);
        scene.getChildren().add(mob1.getPos());
    }

    public static void updateEnemies(double DeltaTime) {
        for(Enemies e: enemies) {
            e.update(DeltaTime);
        }
    }
}
