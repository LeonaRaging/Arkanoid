package com.arkanoid.brick;

import com.arkanoid.core.Ball;
import com.arkanoid.core.Entity;
import com.arkanoid.powerup.PowerUpManager;
import com.arkanoid.Number_and_string_display.ScoreDisplay;
import com.arkanoid.sound.Sound;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.layout.AnchorPane;

public class BrickManager {
  private static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brickRemain;

  public static ArrayList<Brick> getBricks() {
    return bricks;
  }

  public static void createBricks(AnchorPane scene, int level) {
    bricks.clear();
    brickRemain = 0;

    File file = new File("src/main/resources/com/arkanoid/Level/level" + level + ".txt");
    try {
      Scanner sc = new Scanner(file);

      for (int i = 0; i < 26; i++) {
        for (int j = 0; j < 10; j++) {
          int index = sc.nextInt();
          if (index != -1) {
            Brick brick = new Brick(j * 16 + 16, i * 8 + 16, 16, 8, index);
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
            scene.getChildren().add(brick.getImageView());
            bricks.add(brick);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean update(Entity entity, AnchorPane scene) {
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
        Random rand = new Random();
        if (rand.nextDouble() <= 0.25) {
          PowerUpManager.createPowerUps(brick, scene);
        }
        ScoreDisplay.addScore(brick.getScore());
        return true;
      }
      return false;
    });
    return check.get();
  }
}
