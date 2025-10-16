package com.arkanoid.Enemies;

import com.arkanoid.Brick.BrickManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import com.arkanoid.Controller;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EnemiesManager {
    public static ArrayList<Enemies> enemies = new ArrayList<>();

    public static ArrayList<Enemies> getEnemies() {
        return enemies;
    }

    private static class spawnEnemy {
        public int number;
        public int type;
        public double x;
        public double y;

        spawnEnemy(int number, int type, double x, double y) {
            this.number = number;
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    static ArrayList<spawnEnemy>[] spawnEnemies = new ArrayList[16];
    public static boolean gameOver = false;

    public static void initEnemiesManager() throws FileNotFoundException {
        for (int level = 1; level <= 1; level++) {
            File file = new File("src/main/resources/com/arkanoid/Level/level" + level + "enemy.txt");
            Scanner sc = new Scanner(file);
            spawnEnemies[level] = new ArrayList<>();
            while (sc.hasNextLine()) {
                int number = sc.nextInt();
                int type = sc.nextInt();
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                System.out.println(x + " " + y);
                spawnEnemies[level].add(new spawnEnemy(number, type, x, y));
            }
        }
    }

    public static void update(AnchorPane scene, int level) {
        spawnEnemies[level].removeIf(spawnEnemy -> {
//            System.out.println(spawnEnemy.number + " " + BrickManager.brickRemain);
            if (spawnEnemy.number == BrickManager.brickRemain) {
                if (spawnEnemy.type == 0) {
                    TriangleC triangleC = new TriangleC(spawnEnemy.x, spawnEnemy.y, 15, 15);
                    enemies.add(triangleC);
                    scene.getChildren().add(triangleC.getImageView());
                }
                return true;
            }
            return false;
        });
    }

    public static void updateEnemies(AnchorPane scene, double DeltaTime) {

        enemies.removeIf(e -> {
            if(e.update(DeltaTime, scene)) {
                scene.getChildren().remove(e.getImageView());
                scene.getChildren().remove(e.getShape());
                return true;
            }
            return false;
        });

    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        EnemiesManager.gameOver = gameOver;
    }
}
