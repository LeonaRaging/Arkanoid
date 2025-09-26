package com.arkanoid;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class BrickManager {
  public static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brick_remain;

  public static void createBricks(AnchorPane scene) {
    int width = 240;
    int height = 80;

    int spaceCheck = 1;

    brick_remain = 0;

    Color[] ColorSamples = new Color[9];
    ColorSamples[0] = Color.WHITE;
    ColorSamples[1] = Color.GREEN;
    ColorSamples[2] = Color.ORANGE;
    ColorSamples[3] = Color.BLUE;
    ColorSamples[4] = Color.YELLOW;
    ColorSamples[5] = Color.PINK;
    ColorSamples[6] = Color.RED;
    ColorSamples[7] = Color.SILVER;
    ColorSamples[8] = Color.GOLD;

    for (int i = height; i > 0; i -= 20) {
      for (int j = width; j > 0; j -= 10) {
        if (spaceCheck % 2 == 0) {
          Brick brick = new Brick(j, i, 16, 8, 1);
          switch (brick.getHP()) {
            case 1:
              brick_remain++;
              brick.getRectangle().setFill(ColorSamples[6]);
              break;
            case 3:
              brick_remain++;
              brick.getRectangle().setFill(ColorSamples[7]);
              break;
            default:
              brick.getRectangle().setFill(ColorSamples[8]);
              break;
          }
          scene.getChildren().add(brick.getRectangle());
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
        scene.getChildren().remove(brick.getShape());
        PowerUpManager.createPowerUps(brick, scene);
        return true;
      }
      return false;
    });
    return (size != bricks.size());
  }
}
