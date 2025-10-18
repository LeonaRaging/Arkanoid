package com.arkanoid.enemies;

import com.arkanoid.brick.BrickManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import com.arkanoid.Controller;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class EnemiesManager {

  public static ArrayList<Enemies> enemies = new ArrayList<>();

  public static ArrayList<Enemies> getEnemies() {
    return enemies;
  }

  private static class SpawnEnemy {

    public int number;
    public int type;
    public double xpos;
    public double ypos;

    SpawnEnemy(int number, int type, double x, double y) {
      this.number = number;
      this.type = type;
      this.xpos = x;
      this.ypos = y;
    }
  }

  static ArrayList<SpawnEnemy>[] spawnEnemies = new ArrayList[16];
  public static boolean gameOver = false;

  public static void initEnemiesManager() throws FileNotFoundException {
    for (int level = 1; level <= 8; level++) {
      File file = new File("src/main/resources/com/arkanoid/Level/level" + level + "enemy.txt");
      Scanner sc = new Scanner(file);
      spawnEnemies[level] = new ArrayList<>();
      while (sc.hasNextLine()) {
        int number = sc.nextInt();
        int type = sc.nextInt();
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        System.out.println(x + " " + y);
        spawnEnemies[level].add(new SpawnEnemy(number, type, x, y));
      }
    }
  }

  public static void update(AnchorPane scene, int level) {
    spawnEnemies[level].removeIf(spawnEnemy -> {
      System.out.printf("SpawnEnemy number = %d, brick remain = %d \n", spawnEnemy.number, BrickManager.brickRemain);
      if (spawnEnemy.number >= BrickManager.brickRemain) {
        if (spawnEnemy.type == 0) {
          TriangleC triangleC = new TriangleC(spawnEnemy.xpos, spawnEnemy.ypos, 15, 15);
          enemies.add(triangleC);
          scene.getChildren().add(triangleC.getImageView());
        }
        if (spawnEnemy.type == 1) {
          MolecularModel molecularModel = new MolecularModel(spawnEnemy.xpos, spawnEnemy.ypos, 5);
          enemies.add(molecularModel);
          scene.getChildren().add(molecularModel.getImageView());
        }
        if (spawnEnemy.type == 2) {
          MiniSaturn miniSaturn = new MiniSaturn(spawnEnemy.xpos, spawnEnemy.ypos, 8);
          enemies.add(miniSaturn);
          scene.getChildren().add(miniSaturn.getImageView());
        }
        if (spawnEnemy.type == 3) {
          Infinity infinity = new Infinity(spawnEnemy.xpos, spawnEnemy.ypos, 16, 16);
          enemies.add(infinity);
          scene.getChildren().add(infinity.getImageView());
        }
        if (spawnEnemy.type == 4) {
          Bubble bubble = new Bubble(spawnEnemy.xpos, spawnEnemy.ypos, 15, 15);
          enemies.add(bubble);
          scene.getChildren().add(bubble.getImageView());
        }
        return true;
      }
      return false;
    });
  }

  public static void updateEnemies(AnchorPane scene, double deltaTime) {

    double fieldTopY = 16;// in field

    enemies.removeIf(e -> {
      if (e.update(deltaTime, scene)) {
        scene.getChildren().remove(e.getImageView());
        return true;
      }

      double enemyBottomY = e.getImageView().getY() + e.getImageView().getBoundsInLocal().getHeight();

      if (enemyBottomY < fieldTopY) {
        e.getImageView().setVisible(false);
      }
      else {
        e.getImageView().setVisible(true);
      }
      return false;
    });
  }

  public static void removeAllEnemies(AnchorPane scene) {
    enemies.removeIf(e -> {
      scene.getChildren().remove(e.getImageView());
        return true;
    });
  }

  public static boolean isGameOver() {
      return gameOver;
  }

  public static void setGameOver(boolean gameOver) {
      EnemiesManager.gameOver = gameOver;
  };
}
