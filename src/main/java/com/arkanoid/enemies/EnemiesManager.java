package com.arkanoid.enemies;

import com.arkanoid.brick.BrickManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.layout.AnchorPane;

public class EnemiesManager {

  private static ArrayList<Enemies> enemies = new ArrayList<>();

  private static boolean isSpawned = false;

  public static ArrayList<Enemies> getEnemies() {
    return enemies;
  }

  private static class SpawnEnemy {

    public int type;
    public double xpos;
    public double ypos;

    SpawnEnemy(int type, double x, double y) {
      this.type = type;
      this.xpos = x;
      this.ypos = y;
    }
  }

  public static void update(AnchorPane scene) {
    if (BrickManager.brickRemain % 10 != 0) {
      isSpawned = false;
    }
    if (BrickManager.brickRemain % 10 == 0 && !isSpawned) {
      isSpawned = true;
      Random rand = new Random();
      SpawnEnemy spawnEnemy;
      int type = rand.nextInt(6);
      if (type == 3) {
        spawnEnemy = new SpawnEnemy(type, (rand.nextInt(2) == 0 ? 10 : 180), 55);
      } else {
        spawnEnemy = new SpawnEnemy(type, (rand.nextInt(2) == 0 ? 44 : 144), 5);
      }
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
      if (spawnEnemy.type == 5) {
        RedBlob redBlob = new RedBlob(spawnEnemy.xpos, spawnEnemy.ypos, 8);
        enemies.add(redBlob);
        scene.getChildren().add(redBlob.getImageView());
      }
    }
  }

  public static void updateEnemies(AnchorPane scene, double deltaTime) {

    double fieldTopY = 16; // in field

    enemies.removeIf(e -> {
      if (e.update(deltaTime, scene)) {
        scene.getChildren().remove(e.getImageView());
        return true;
      }

      double enemyBottomY = e.getImageView().getY()
          + e.getImageView().getBoundsInLocal().getHeight();

      if (enemyBottomY < fieldTopY) {
        e.getImageView().setVisible(false);
      } else {
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
}
