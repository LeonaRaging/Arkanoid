package com.arkanoid.Brick;

import com.arkanoid.Core.Ball;
import com.arkanoid.Core.Entity;
import com.arkanoid.PowerUp.PowerUpManager;
import com.arkanoid.Sound.Sound;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.layout.AnchorPane;
import com.arkanoid.Score.ScoreDisplay;

public class BrickManager {
  private static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brick_remain;

  public static ArrayList<Brick> getBricks() {
    return bricks;
  }

  public static void createBricks(AnchorPane scene) {
    brick_remain = 0;

    File file = new File("src/main/resources/com/arkanoid/Level/level1.txt");
    try {
      Scanner sc = new Scanner(file);

      for (int i = 0; i < 26; i++) {
        for (int j = 0; j < 10; j++) {
          int index = sc.nextInt();
          if (index != -1) {
            Brick brick = new Brick(j * 16 + 16, i * 8 + 16, 16, 8, index);
            switch (brick.getHP()) {
              case 1:
                brick_remain++;
                break;
              case 3:
                brick_remain++;
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
          if (brick.getHP() == 0) {
            Sound.playBounceBrick();
          } else {
            Sound.playBounceBrickSilverGold();
          }
        }
      }
      if (brick.getHP() == 0) {
        BrickManager.brick_remain--;
        scene.getChildren().remove(brick.getImageView());
        PowerUpManager.createPowerUps(brick, scene);
        ScoreDisplay.addScore(brick.getScore());
        return true;
      }
      return false;
    });
    return check.get();
  }
}
