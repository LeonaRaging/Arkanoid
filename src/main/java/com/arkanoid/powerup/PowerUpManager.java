package com.arkanoid.powerup;

import com.arkanoid.Controller;
import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import com.arkanoid.core.Paddle;
import com.arkanoid.sound.Sound;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class PowerUpManager {

  private static ArrayList<PowerUp> powerUps = new ArrayList<>();
  private static ArrayList<Entity> projectiles = new ArrayList<>();

  private static int numberOfPowerUps = 4;
  private static int shootCooldown;

  public static int[] powerUpState = new int[numberOfPowerUps];

  public static ArrayList<PowerUp> getPowerUps() {
    return powerUps;
  }

  public static ArrayList<Entity> getProjectiles() {
    return projectiles;
  }

  public static void createPowerUps(Brick brick, AnchorPane scene) {
    Random rand = new Random();
    PowerUp powerUp = new PowerUp(brick.getRectangle().getX(), brick.getRectangle().getY(),
        brick.getRectangle().getWidth(), brick.getRectangle().getHeight(), rand.nextInt(4));
    powerUps.add(powerUp);

    scene.getChildren().add(powerUp.getImageView());
  }

  public static void movePowerUps(AnchorPane scene) {
    for (PowerUp powerUp : powerUps) {
      powerUp.update(scene);
    }
  }

  public static void checkCollisionPowerUps(Paddle paddle, AnchorPane scene) {
    powerUps.removeIf(powerUp -> {
      if (powerUp.getShape().getBoundsInParent()
          .intersects(paddle.getShape().getBoundsInParent())) {
        if (powerUp.getType() == 0) {
          powerUpState[0] = 3;
        }
        if (powerUp.getType() == 1) {
          powerUpState[1] = 5;
        }
        if (powerUp.getType() == 2) {
          paddle.getRectangle().setWidth(48);
          paddle.setState(1);
          Sound.playPowerUpE();
        }
        if (powerUp.getType() == 3) {
          Ball currentBall = BallManager.getBalls().getFirst();
          for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
              if (i != 0 && j != 0) {
                if (2 * i == currentBall.getDeltaX() && 2 * j == currentBall.getDeltaY()) {
                  continue;
                }
                Ball ball = new Ball(0, 0, 2.5);
                ball.getCircle().setLayoutX(currentBall.getCircle().getLayoutX());
                ball.getCircle().setLayoutY(currentBall.getCircle().getLayoutY());
                ball.setDeltaX(i);
                ball.setDeltaY(j);
                BallManager.getBalls().add(ball);
                scene.getChildren().add(ball.getImageView());
              }
            }
          }
        }
        scene.getChildren().remove(powerUp.getImageView());
        return true;
      }
      return false;
    });
  }

  public static void update(Paddle paddle, AnchorPane scene) {
    projectiles.removeIf(entity -> {
      if (BrickManager.update(entity, scene)) {
        scene.getChildren().remove(entity.getImageView());
        return true;
      }

      if (entity.getRectangle().getY() <= Controller.field.getRectangle().getY()) {
        scene.getChildren().remove(entity.getImageView());
        return true;
      }

      return false;
    });

    for (Entity entity : projectiles) {
      entity.getRectangle().setY(entity.getRectangle().getY() - 2);
      entity.getImageView().setX(entity.getRectangle().getX());
      entity.getImageView().setY(entity.getRectangle().getY());
    }

    if (shootCooldown > 0) {
      shootCooldown--;
    }
    if (powerUpState[1] > 0 && shootCooldown == 0 && Controller.pressedKeys.contains(
        KeyCode.SPACE)) {
      double paddleWidth = paddle.getRectangle().getWidth();

      Entity projectile1 = new Entity(paddle.getRectangle().getX() + paddleWidth / 2 - 5,
          paddle.getRectangle().getY(), 8, 16);
      Entity projectile2 = new Entity(paddle.getRectangle().getX() + paddleWidth / 2 + 5,
          paddle.getRectangle().getY(), 8, 16);
      Image image = new Image(
          PowerUpManager.class.getResource("/com/arkanoid/paddle/bullet.png").toExternalForm());
      projectile1.getImageView().setImage(image);
      projectile2.getImageView().setImage(image);

      projectiles.add(projectile1);
      projectiles.add(projectile2);

      scene.getChildren().add(projectile1.getImageView());
      scene.getChildren().add(projectile2.getImageView());

      powerUpState[1]--;
      shootCooldown = 30;
      Sound.playShoot();
    }
  }

  public static void resetPower() {
    for (int i = 0; i < numberOfPowerUps; i++) {
      powerUpState[i] = 0;
    }
    projectiles.clear();
    powerUps.clear();
  }

}
