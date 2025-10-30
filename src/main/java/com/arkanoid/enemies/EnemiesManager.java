package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import com.arkanoid.enemies.bosses.DohFace;
import javafx.scene.layout.AnchorPane;
import com.arkanoid.enemies.bosses.GiantCentipedeBoss;

public class EnemiesManager {

  public static ArrayList<Enemies> enemies = new ArrayList<>();
  public static ArrayList<Enemies> newEnemies = new ArrayList<>();
  public static boolean gameOver = false;
  private static boolean isSpawned = false;
  private static int deadCooldown = 0;

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
    switch (Controller.getLevel()) {
      case 5:
        if (enemies.isEmpty()) {
          BrickManager.setBrickRemain(BrickManager.getBrickRemain() + 1);
          GiantCentipedeBoss bossLevel5 = new GiantCentipedeBoss(90, 80, 13, scene);
          enemies.add(bossLevel5);
        }
        break;
      case 15:
        if (enemies.isEmpty()) {
          BrickManager.setBrickRemain(BrickManager.getBrickRemain() + 1);
          DohFace bossLevel15 = new DohFace(scene);
          scene.getChildren().add(bossLevel15.getImageView());
          enemies.add(bossLevel15);
        }
        break;
      default:
        if (BrickManager.getBrickRemain() % 10 != 0) {
          isSpawned = false;
        }
        if (BrickManager.getBrickRemain() % 10 == 0 && !isSpawned) {
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
        break;
    }
  }

  public static void addEnemy(Enemies enemy) {
    newEnemies.add(enemy);
  }

  // remove all enemies
  public static void clear(AnchorPane scene) {
    for (Enemies enemy : enemies) {
      enemy.clear(scene);
    }
    for (Enemies enemy : newEnemies) {
      enemy.clear(scene);
    }
    enemies.clear();
    newEnemies.clear();
  }

  public static void updateEnemies(AnchorPane scene, double deltaTime) {
    deadCooldown--;

    enemies.removeIf(e -> {
      if (e.update(deltaTime, scene)) {
        e.clear(scene);
        return true;
      }
      return false;
    });

    enemies.addAll(newEnemies);
    newEnemies.clear();
  }

  public static boolean isGameOver() {
    return gameOver;
  }

  public static void setGameOver(boolean gameOver) {
    if (gameOver) {
      if (deadCooldown <= 0) {
        EnemiesManager.gameOver = gameOver;
        deadCooldown = 100 * 2; // 2 seconds invulnerable
      }
    } else {
      EnemiesManager.gameOver = gameOver;
    }
  }
}
