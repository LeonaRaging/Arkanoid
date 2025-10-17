package com.arkanoid.enemies;

import com.arkanoid.brick.BrickManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.layout.AnchorPane;

public class EnemiesManager {

  private static ArrayList<Enemies> enemies = new ArrayList<>();

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
        spawnEnemies[level].add(new SpawnEnemy(number, type, x, y));
      }
    }
  }

  public static void update(AnchorPane scene, int level) {
    spawnEnemies[level].removeIf(spawnEnemy -> {
      if (spawnEnemy.number == BrickManager.brickRemain) {
        if (spawnEnemy.type == 0) {
          TriangleC triangleC = new TriangleC(spawnEnemy.xpos, spawnEnemy.ypos, 15, 15);
          enemies.add(triangleC);
          scene.getChildren().add(triangleC.getImageView());
        }
        return true;
      }
      return false;
    });
  }

  public static void updateEnemies(AnchorPane scene, double deltaTime) {

    enemies.removeIf(e -> {
      if (e.update(deltaTime, scene)) {
        scene.getChildren().remove(e.getImageView());
        return true;
      }
      return false;
    });
  }
}
