package com.arkanoid;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class BrickManager {
  public static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brick_remain;

  public static void createBricks(AnchorPane scene) {
    int width = 160;
    int height = 80;

    int spaceCheck = 1;

    brick_remain = 0;

    for (int i = height; i > 10; i -= 20) {
      for (int j = width; j > 10; j -= 10) {
        if (spaceCheck % 2 == 0) {
          Brick brick = new Brick(j, i, 16, 8, 1);
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
    int size = bricks.size();
    bricks.removeIf(brick -> {
      brick.checkCollisionBrick(entity);
      if (brick.getHP() == 0) {
        BrickManager.brick_remain--;
        scene.getChildren().remove(brick.getImageView());
        PowerUpManager.createPowerUps(brick, scene);
        return true;
      }
      return false;
    });
    return (size != bricks.size());
  }
}
