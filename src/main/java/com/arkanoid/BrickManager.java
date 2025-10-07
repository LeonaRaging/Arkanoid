package com.arkanoid;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.layout.AnchorPane;

public class BrickManager {
  private static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brick_remain;

  public static ArrayList<Brick> getBricks() {
    return bricks;
  }

  public static void createBricks(AnchorPane scene) {
    int width = 160;
    int height = 80;

    int spaceCheck = 1;

    brick_remain = 0;

    for (int i = height; i > 10; i -= 20) {
      for (int j = width; j > 10; j -= 10) {
        if (spaceCheck % 2 == 0) {
          Brick brick = new Brick(j, i, 16, 8, 2);
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
        spaceCheck++;
      }
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
        return true;
      }
      return false;
    });
    return check.get();
  }
}
