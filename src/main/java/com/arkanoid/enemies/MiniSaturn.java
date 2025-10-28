package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.powerup.PowerUpManager;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class MiniSaturn extends Enemies {

  private int moveCooldown = 0;
  private final Image[][] images = new Image[2][6];
  private int state = 0;
  private int imageState;
  private int imageCooldown;

  public MiniSaturn(double x, double y, double r) {
    super(x, y, r);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 6 - i; j++) {
        images[i][j] = new Image(getClass().getResource(
            "/com/arkanoid/enemy/MiniSaturn/MiniSaturn" + i + j + ".png").toExternalForm());
      }
    }
    state = 0;
    imageState = 0;
    imageView.setImage(images[state][imageState]);
    imageCooldown = 20;
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {
    switch (state) {
      case 0:
        if (moveCooldown == 0) {
          moveCooldown = (int) (Math.random() * 300) + 100;
        }

        if (moveCooldown == 100) {
          deltaX = (Math.random() - 0.5) * 1.2;
          deltaY = (Math.random() - 0.5) * 1.2;
          for (int i = 0; i < 2; i++) {
            if (isAbove(Controller.gates[i].getRectangle())) {
              deltaY = Math.abs(deltaY);
            }
          }
        }

        if (moveCooldown <= 100) {
          this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
          this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
          if (this.checkCollisionBrick() || this.checkCollisionScene()
              || this.checkCollisionEnemy()) {
            this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
            this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
            moveCooldown = 1;
          }
        }

        moveCooldown--;

        if (this.checkCollisionBall()) {
          state = 1;
          imageCooldown = 20;
          imageState = 0;
          moveCooldown = imageCooldown * 5;
        } else {
          imageCooldown--;
          if (imageCooldown == 0) {
            imageState++;
            imageState %= 6;
            imageCooldown = 20;
          }
        }
        break;
      case 1:
        imageCooldown--;
        moveCooldown--;
        if (moveCooldown == 0) {
          return true;
        }
        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 20;
        }
        if (imageCooldown == 19) {
          if (imageState == 0) {
            this.getCircle().setRadius(this.getCircle().getRadius() * 2);
            BrickManager.getBricks().removeIf(brick -> {
              if (this.getCircle().getBoundsInParent()
                  .intersects(brick.getShape().getBoundsInParent())) {
                BrickManager.brickRemain--;
                scene.getChildren().remove(brick.getImageView());
                scene.getChildren().remove(brick.shadow);
                PowerUpManager.createPowerUps(brick, scene);
                return true;
              }
              return false;
            });

          }
          for (Ball ball : BallManager.getBalls()) {
            if (ball.getShape().getBoundsInParent()
                .intersects(this.getShape().getBoundsInParent())) {
              ball.updateX(1);
              ball.updateY(1);
            }
          }
        }
        break;
      default:
        break;
    }

    imageView.setImage(images[state][imageState]);
    imageView.setX(this.getCircle().getCenterX() - images[state][imageState].getWidth() / 2);
    imageView.setY(this.getCircle().getCenterY() - images[state][imageState].getHeight() / 2);

    return outScene();
  }


}
