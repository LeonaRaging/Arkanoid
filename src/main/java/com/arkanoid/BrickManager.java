package com.arkanoid;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BrickManager {
  public static ArrayList<Brick> bricks = new ArrayList<>();

  public static int brick_remain;

  public static void createBricks(AnchorPane scene) {
    int width = 560;
    int height = 200;

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

    for (int i = height; i > 0; i -= 50) {
      for (int j = width; j > 0; j -= 25) {
        if (spaceCheck % 2 == 0) {
          Brick brick = new Brick(j, i, 30, 30, 1);
          switch (brick.getHP()) {
            case 1:
              brick_remain++;
              brick.getPos().setFill(ColorSamples[(int)(Math.random() * 7)]);
              break;
            case 3:
              brick_remain++;
              brick.getPos().setFill(ColorSamples[7]);
              break;
            default:
              brick.getPos().setFill(ColorSamples[8]);
              break;
          }
          scene.getChildren().add(brick.getPos());
          bricks.add(brick);
        }
        spaceCheck++;
      }
    }
  }

  public static void update(Circle circle, AnchorPane scene) {
    bricks.removeIf(brick -> {
      brick.checkCollisionBrick(circle);
      if (brick.getHP() == 0) {
        BrickManager.brick_remain--;
        scene.getChildren().remove(brick.getPos());
        PowerUpManager.createPowerUps(brick, scene);
        return true;
      }
      return false;
    });
  }
}
