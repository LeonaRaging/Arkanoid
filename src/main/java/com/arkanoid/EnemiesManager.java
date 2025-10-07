package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EnemiesManager {
    public static ArrayList<Enemies> enemies = new ArrayList<>();

    public static void CreateEnemies(AnchorPane scene) {
        /*TriangleC mob1 = new TriangleC(70, 100, 15, 15);
        enemies.add(mob1);
        mob1.getRectangle().setFill(Color.BLACK);
        scene.getChildren().add(mob1.getRectangle());
        scene.getChildren().add(mob1.getImageView());*/

        /*MiniSaturn mob2 = new MiniSaturn(65, 100, 8);
        enemies.add(mob2);
        scene.getChildren().add(mob2.getImageView());
        mob2.getCircle().setFill(Color.GREEN);
        scene.getChildren().add(mob2.getCircle());*/

        /*RedBlob mob3 = new RedBlob(108, 100, 8);
        enemies.add(mob3);
        //mob3.getCircle().setFill(Color.RED);
        //scene.getChildren().add(mob3.getCircle());
        scene.getChildren().add(mob3.getImageView());*/

        Bubble mob4 = new Bubble(36, 136, 15, 15); // 70 50 30 30
        enemies.add(mob4);
        scene.getChildren().add(mob4.getImageView());

//        MolecularModel mob5 = new MolecularModel(55, 100, 5);
//        enemies.add(mob5);
//        scene.getChildren().add(mob5.getImageView());

//        Infinity mob6 = new Infinity(70, 100, 16, 16);
//        enemies.add(mob6);
//        scene.getChildren().add(mob6.getImageView());

    }

    public static void updateEnemies(AnchorPane scene, double DeltaTime) {

        enemies.removeIf(e -> {
            if(e.update(DeltaTime, scene)) {
                scene.getChildren().remove(e.getImageView());
                return true;
            }
            return false;
        });
    }
}
