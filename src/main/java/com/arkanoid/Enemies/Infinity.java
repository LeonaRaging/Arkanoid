package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.sound.Sound;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Infinity extends Enemies {
  int moveCooldown = 450;
  private final Image[][] images = new Image[3][12];
  private int state = 0;
  private int imageState;
  private int imageCooldown;

  public Infinity(double x, double y, double w, double h) {
    super(x, y, w, h);
    deltaX = 0.2;
    deltaY = 0;
    for (int i = 0; i < 4; i++) {
      images[0][i] = new Image(getClass().getResource(
          "/com/arkanoid/enemy/Infinity/Infinity0" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 12; i++) {
      images[1][i] = new Image(getClass().getResource(
          "/com/arkanoid/enemy/TriangleC/TriangleC1" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 2; i++) {
      images[2][i] = new Image(getClass().getResource(
          "/com/arkanoid/enemy/Infinity/Infinity2" + i + ".png").toExternalForm());
    }
    state = 0;
    imageState = 0;
    imageCooldown = 20;
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {
    System.out.println(state);
    if (isLeft(Controller.gates[2].getRectangle())) {
      deltaX = 0.2;
    }
    if (isRight(Controller.gates[3].getRectangle())) {
      deltaX = -0.2;
    }
    switch (state) {
      case 0:
        if (moveCooldown == 0) {
          deltaX = -deltaX;
          moveCooldown = 450;
        }

        if (this.checkCollisionBall()) {
          state = 1;
          imageCooldown = 20;
          imageState = 0;
          moveCooldown = imageCooldown * 12;
          Sound.playExplode();
        } else {
          imageCooldown--;
          if (imageCooldown == 0) {
            imageState++;
            imageState %= 4;
            imageCooldown = 20;
          }

          this.getRectangle().setX(this.getRectangle().getX() + deltaX);
          if (this.checkCollisionScene() || this.checkCollisionEnemy()
              || this.checkCollisionBrick()) {
            this.getRectangle().setX(this.getRectangle().getX() - deltaX);
          }
          moveCooldown--;
          if (moveCooldown == 299) {
            state = 2;
            imageCooldown = 20;
            imageState = 0;
            Sound.playInfinity();
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
        break;
      case 2:
        Rectangle rectangle = new Rectangle(this.getRectangle().getX() - 6,
            this.getRectangle().getY() - 6,
            this.getRectangle().getWidth() + 6, this.getRectangle().getHeight() + 6);
        for (Ball ball : BallManager.getBalls()) {
          if (ball.getShape().getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
            Circle circle = ball.getCircle();
            boolean rightBorder = circle.getLayoutX() >= ((rectangle.getX() + rectangle.getWidth())
                - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (rectangle.getX() + circle.getRadius());
            boolean bottomBorder =
                circle.getLayoutY() >= ((rectangle.getY() + rectangle.getHeight())
                    - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (rectangle.getY() + circle.getRadius());

            if (rightBorder || leftBorder) {
              ball.updateX(rightBorder ? -1 : 1);
            }
            if (bottomBorder || topBorder) {
              ball.updateY(bottomBorder ? -1 : 1);
            }
          }
        }
        imageCooldown--;
        moveCooldown--;
        if (moveCooldown <= 100) {
          state = 0;
          imageState = 0;
          imageCooldown = 20;
        }
        if (imageCooldown == 0) {
          imageState++;
          imageState %= 2;
          imageCooldown = 20;
        }
        break;
      default:
        break;
    }

    imageView.setImage(images[state][imageState]);
    imageView.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2
        - images[state][imageState].getWidth() / 2);
    imageView.setY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2
        - images[state][imageState].getHeight() / 2);

    return false;
  }
}
