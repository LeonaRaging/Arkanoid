package com.arkanoid.brick;

import static com.arkanoid.Controller.field;
import static com.arkanoid.Controller.score;

import com.arkanoid.core.Ball;
import com.arkanoid.core.Entity;
import com.arkanoid.powerup.PowerUpManager;
import com.arkanoid.sound.Sound;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.layout.AnchorPane;

public class BrickManager {

  private BrickManager() {
    throw new IllegalStateException("Utility class");
  }

  private static ArrayList<Brick> bricks = new ArrayList<>();

  private static int brickRemain;

  public static int getBrickRemain() {
    return brickRemain;
  }

  public static void setBrickRemain(int value) {
    brickRemain = value;
  }

  public static ArrayList<Brick> getBricks() {
    return bricks;
  }

  private static void readBricks(AnchorPane scene, int level) throws FileNotFoundException {
    File file = new File("src/main/resources/com/arkanoid/Level/level" + level + ".txt");
    Scanner sc = new Scanner(file);
    for (int i = 0; i < 26; i++) {
      for (int j = 0; j < 10; j++) {
        int index = sc.nextInt();
        if (index != -1) {
          Brick brick = new Brick((double) j * 16 + 16, (double) i * 8 + 16, 16, 8, index);
          switch (brick.getHp()) {
            case 1:
              brickRemain++;
              break;
            case 3:
              brickRemain++;
              break;
            default:
              break;
          }
          bricks.add(brick);
          if (brick.getRectangle().getX() + 1.5 * brick.getRectangle().getWidth()
              <= field.getRectangle().getX() + field.getRectangle().getWidth()) {
            scene.getChildren().add(brick.getShadow());
          }
        }
      }
    }
    sc.close();
  }

  public static void createBricks(AnchorPane scene, int level) {
    bricks.clear();
    brickRemain = 0;

    if (level == 5 || level == 15) {
      return;
    }

    try {
      readBricks(scene, level);

      for (Brick brick : bricks) {
        scene.getChildren().add(brick.getImageView());
      }
      Sound.playEndLevel();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean update(Entity entity, AnchorPane scene) {
    PowerUpManager.powerUpCooldown--;
    AtomicBoolean check = new AtomicBoolean(false);
    bricks.removeIf(brick -> {
      if (brick.checkCollisionBrick(entity)) {
        check.set(true);
        if (entity instanceof Ball) {
          if (brick.getHp() == 0) {
            Sound.playBounceBrick();
          } else {
            Sound.playBounceBrickSilverGold();
          }
        }
      }

      if (brick.getHp() == 0) {
        BrickManager.brickRemain--;
        scene.getChildren().remove(brick.getImageView());
        scene.getChildren().remove(brick.getShadow());
        if (PowerUpManager.powerUpCooldown <= 0) {
          PowerUpManager.createPowerUps(brick, scene);
          PowerUpManager.powerUpCooldown = 500;
        }
        score.addScore(brick.getScore());
        return true;
      }
      return false;
    });
    return check.get();
  }
}
