package com.arkanoid;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class Paddle extends Entity {
  private final Image[] images =  new Image[4];
  private int imageState;
  private int imageCooldown;

  public Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
    for (int i = 0; i < images.length; i++) {
      images[i] = new Image(getClass().getResource("Paddle/paddle" + i + ".png").toExternalForm());
    }
    imageState = 0;
    imageCooldown = 20;
  }

  public void update(Rectangle rect) {

    double xPos = this.getRectangle().getX();

    if (Controller.pressedKeys.contains(KeyCode.RIGHT)) {
      xPos += 2;
    }

    if (Controller.pressedKeys.contains(KeyCode.LEFT)) {
      xPos -= 2;
    }

    if (xPos < rect.getX()) {
      this.getRectangle().setX(rect.getX());
    } else  if (xPos + this.getRectangle().getWidth() >= rect.getX() + rect.getWidth()) {
      this.getRectangle().setX(rect.getX() + rect.getWidth() - this.getRectangle().getWidth());
    } else {
      this.getRectangle().setX(xPos);
    }

    imageCooldown--;
    if (imageCooldown == 0) {
      imageState++;
      imageState %= 4;
      imageCooldown = 20;
    }
    imageView.setImage(images[imageState]);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void checkCollisionPaddle(Ball ball) {
    if (ball.getCircle().getBoundsInParent().intersects(this.getRectangle().getBoundsInParent())) {
      ball.deltaY *= -1;
    }
  }
}
