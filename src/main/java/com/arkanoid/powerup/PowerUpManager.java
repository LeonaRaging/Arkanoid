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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class PowerUpManager {

  private static ArrayList<PowerUp> powerUps = new ArrayList<>();
  private static ArrayList<Entity> projectiles = new ArrayList<>();

  private static int numberOfPowerUps = 4;
  private static int shootCooldown;
  public static int powerUpCooldown = 30;

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

    powerUpCooldown = 30;
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
        if (powerUp.getType() != 2) {
          for (int i = 0; i < 4; i++) {
            if (powerUpState[i] == 1) {
              switch (i) {
                case 0:
                  Ball ball = BallManager.getBalls().getFirst();
                  for (ImageView imageView : ball.imageViews) {
                    scene.getChildren().remove(imageView);
                  }
                  scene.getChildren().add(ball.getImageView());
                  break;
                case 1:
                  paddle.setState(3);
                  break;
                case 2:
                  break;
                case 3:
                  while (BallManager.getBalls().size() > 1) {
                    Ball ball2 = BallManager.getBalls().getFirst();
                    scene.getChildren().remove(ball2.getImageView());
                    BallManager.getBalls().remove(ball2);
                  }
                  break;
              }
              powerUpState[i] = 0;
            }
          }
        }


        if (powerUp.getType() == 0) {
          if (powerUpState[0] == 0) {
            powerUpState[0] = 1;
            Ball ball = BallManager.getBalls().getFirst();
            scene.getChildren().remove(ball.getImageView());
            for (int i = 0; i < 6; i++) {
              scene.getChildren().add(ball.imageViews[i]);
            }
          }
        }
        if (powerUp.getType() == 1) {
          paddle.setState(2);
          paddle.getRectangle().setWidth(32);
        }
        if (powerUp.getType() == 2 && powerUpState[1] == 0) {
          paddle.getRectangle().setWidth(48);
          paddle.setState(1);
          Sound.playPowerUpE();
        }
        if (powerUp.getType() == 3 && !BallManager.getBalls().isEmpty()) {
          Ball currentBall = BallManager.getBalls().getFirst();
          int [] dx = { 0,  1, 1, 1, 0, -1, -1, -1};
          int [] dy = {-1, -1, 0, 1, 1,  1,  0, -1};
          for (int i = 0; i < 8; i++) {
            Ball ball = new Ball(0, 0, 2.5);
            ball.getCircle().setLayoutX(currentBall.getCircle().getLayoutX());
            ball.getCircle().setLayoutY(currentBall.getCircle().getLayoutY());
            ball.setDeltaX(dx[i]);
            ball.setDeltaY(dy[i]);
            BallManager.getBalls().add(ball);
            scene.getChildren().add(ball.getImageView());
          }
        }
        powerUpState[powerUp.getType()] = 1;
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
          paddle.getRectangle().getY(), 4, 12);
      Entity projectile2 = new Entity(paddle.getRectangle().getX() + paddleWidth / 2 + 5,
          paddle.getRectangle().getY(), 4, 12);
      Image image = new Image(
          PowerUpManager.class.getResource("/com/arkanoid/paddle/bullet.png").toExternalForm());
      projectile1.getImageView().setImage(image);
      projectile2.getImageView().setImage(image);

      projectiles.add(projectile1);
      projectiles.add(projectile2);

      scene.getChildren().add(projectile1.getImageView());
      scene.getChildren().add(projectile2.getImageView());

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
