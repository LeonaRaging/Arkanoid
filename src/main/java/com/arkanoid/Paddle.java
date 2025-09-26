package com.arkanoid;

import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class Paddle extends Entity {

  public Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
  }

  @Override
  public void update(AnchorPane scene) {
    Bounds bounds = scene.getBoundsInLocal();

    double xPos = this.getRectangle().getX();

    if (Controller.pressedKeys.contains(KeyCode.RIGHT)) {
      xPos += 2;
    }

    if (Controller.pressedKeys.contains(KeyCode.LEFT)) {
      xPos -= 2;
    }

    if (xPos < bounds.getMinX()) {
      this.getRectangle().setX(0);
    } else  if (xPos + this.getRectangle().getWidth() > bounds.getMaxX()) {
      this.getRectangle().setX(bounds.getMaxX() - this.getRectangle().getWidth());
    } else {
      this.getRectangle().setX(xPos);
    }

  }

  public void checkCollisionPaddle(Ball ball) {
    if (ball.getCircle().getBoundsInParent().intersects(this.getRectangle().getBoundsInParent())) {
      ball.deltaY *= -1;
    }
  }
}
