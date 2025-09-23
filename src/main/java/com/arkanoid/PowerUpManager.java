package com.arkanoid;

import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class PowerUpManager {
  public static ArrayList<PowerUp> powerUps = new ArrayList<>();
  public static ArrayList<Entity> projectiles = new ArrayList<>();

  private static int numberOfPowerUps = 2;
  public static int shootCooldown;

  public static int[] powerUpState = new int [numberOfPowerUps];

  public static void createPowerUps(Brick brick, AnchorPane scene) {
    PowerUp powerUp = new PowerUp(brick.getRectangle().getX(), brick.getRectangle().getY(), brick.getRectangle().getWidth(), 10, 1);
    powerUps.add(powerUp);

    scene.getChildren().add(powerUp.getShape());
  }

  public static void movePowerUps(AnchorPane scene) {
    for (PowerUp powerUp : powerUps) {
      powerUp.update(scene);
    }
  }

  public static void checkCollisionPowerUps(Paddle paddle, AnchorPane scene) {
    for (PowerUp powerUp : powerUps) {
      if (powerUp.getShape().getBoundsInParent().intersects(paddle.getShape().getBoundsInParent())) {
        if (powerUp.getType() == 0) {
          powerUpState[0] = 3;
        }
        if (powerUp.getType() == 1) {
          powerUpState[1] = 5;
        }
        scene.getChildren().remove(powerUp.getShape());
      }
    }
  }

  public static void update(Paddle paddle, AnchorPane scene) {
    for (Entity entity : projectiles) {

      entity.getRectangle().setY(entity.getRectangle().getY() - 4);

      if (BrickManager.update(entity, scene)) {
        scene.getChildren().remove(entity.getShape());
      }

      Bounds bounds = scene.getBoundsInLocal();
      if (entity.getRectangle().getY() <= bounds.getMinY()) {
        scene.getChildren().remove(entity.getShape());
      }
    }

    if (shootCooldown > 0) shootCooldown--;
    if (powerUpState[1] > 0 && shootCooldown == 0 && Controller.pressedKeys.contains(KeyCode.SPACE)) {
      double paddleWidth = paddle.getRectangle().getWidth();

      Entity projectile1 = new Entity(paddle.getRectangle().getX() + paddleWidth / 2 - 10 , paddle.getRectangle().getY(), 5, 20);
      Entity projectile2 = new Entity(paddle.getRectangle().getX() + paddleWidth / 2 + 10, paddle.getRectangle().getY(), 5, 20);

      projectiles.add(projectile1);
      projectiles.add(projectile2);

      scene.getChildren().add(projectile1.getShape());
      scene.getChildren().add(projectile2.getShape());

      powerUpState[1]--;
      shootCooldown = 30;
    }
  }

}
