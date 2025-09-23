package com.arkanoid;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

public class PowerUpManager {
  private static ArrayList<PowerUp> powerUps = new ArrayList<>();
  private static int numberOfPowerUps = 1;
  private static int[] powerUpState = new int [numberOfPowerUps];

  public static void createPowerUps(Brick brick, AnchorPane scene) {
    PowerUp powerUp = new PowerUp(brick.getPos().getX(), brick.getPos().getY(), brick.getPos().getWidth(), 10, 0);
    powerUps.add(powerUp);

    scene.getChildren().add(powerUp.getPos());
  }

  public static void movePowerUps(AnchorPane scene) {
    for (PowerUp powerUp : powerUps) {
      powerUp.update(scene);
    }
  }

  public static void checkCollisionPowerUps(Paddle paddle, AnchorPane scene) {
    for (PowerUp powerUp : powerUps) {
      if (powerUp.getPos().getBoundsInParent().intersects(paddle.getPos().getBoundsInParent())) {
        powerUpState[powerUp.getType()] = 1;
        scene.getChildren().remove(powerUp.getPos());
      }
    }

  }
}
