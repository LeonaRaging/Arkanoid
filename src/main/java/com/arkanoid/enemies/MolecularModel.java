package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.sound.Sound;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class MolecularModel extends Enemies {

  double moveSpeed = 0.2;
  int moveCooldown = 150;
  private final Image[][] images = new Image[2][22];
  private int state = 0;
  private int imageState;
  private int imageCooldown;

  public MolecularModel(double x, double y, double r) {
    super(x, y, r);
    deltaX = moveSpeed;
    deltaY = moveSpeed;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < (i == 0 ? 22 : 3); j++) {
        images[i][j] = new Image(getClass().getResource(
                "/com/arkanoid/enemy/MolecularModel/MolecularModel" + i + j + ".png")
            .toExternalForm());
      }
    }
    state = 0;
    imageState = 0;
    imageCooldown = 20;
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {
    switch (state) {
      case 0:
        if (this.checkCollisionBall()) {
          state = 1;
          imageCooldown = 20;
          imageState = 0;
          moveCooldown = imageCooldown * 3;
          Sound.playExplode();
        } else {
          imageCooldown--;
          if (imageCooldown == 0) {
            imageState++;
            imageState %= 22;
            imageCooldown = 20;
          }
        }

        if (moveCooldown == 0) {
          deltaX = -deltaX;
          moveCooldown = 150;
        } else {
          this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
          if (this.checkCollisionScene() || this.checkCollisionBrick()
              || this.checkCollisionEnemy()) {
            this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
          }
          this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
          if (this.checkCollisionScene() || this.checkCollisionBrick()
              || this.checkCollisionEnemy()) {
            this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
          }
          moveCooldown--;
        }
        break;
      case 1:
        if (imageCooldown == 20 && imageState == 0) {
          // Ball 1 (-1, -1)
          Ball ball1 = new Ball(0, 0, 2.5);
          ball1.getCircle()
              .setLayoutX(this.getCircle().getCenterX() - 2 * this.getCircle().getRadius());
          ball1.getCircle()
              .setLayoutY(this.getCircle().getCenterY() - 2 * this.getCircle().getRadius());
          ball1.setDeltaX(-1);
          ball1.setDeltaY(-1);
          ball1.ballType = 1;
          BallManager.getBalls().add(ball1);
          scene.getChildren().add(ball1.getImageView());
          // Ball 2 (1, -1)
          Ball ball2 = new Ball(0, 0, 2.5);
          ball2.getCircle()
              .setLayoutX(this.getCircle().getCenterX() + 2 * this.getCircle().getRadius());
          ball2.getCircle()
              .setLayoutY(this.getCircle().getCenterY() - 2 * this.getCircle().getRadius());
          ball2.setDeltaX(1);
          ball2.setDeltaY(-1);
          ball2.ballType = 2;
          BallManager.getBalls().add(ball2);
          scene.getChildren().add(ball2.getImageView());
          // Ball 3 (1, 1)
          Ball ball3 = new Ball(0, 0, 2.5);
          ball3.getCircle()
              .setLayoutX(this.getCircle().getCenterX() + 2 * this.getCircle().getRadius());
          ball3.getCircle()
              .setLayoutY(this.getCircle().getCenterY() + 2 * this.getCircle().getRadius());
          ball3.setDeltaX(1);
          ball3.setDeltaY(1);
          ball3.ballType = 3;
          BallManager.getBalls().add(ball3);
          scene.getChildren().add(ball3.getImageView());
        }
        imageCooldown--;
        moveCooldown--;
        if (moveCooldown == 0) {
          return true;
        }
        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 20;
        }
        break;
      default:
        break;
    }

    imageView.setImage(images[state][imageState]);
    imageView.setX(this.getCircle().getCenterX() - images[state][imageState].getWidth() / 2);
    imageView.setY(this.getCircle().getCenterY() - images[state][imageState].getHeight() / 2);

    return false;
  }
}
