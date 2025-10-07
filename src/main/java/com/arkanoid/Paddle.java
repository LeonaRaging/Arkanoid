package com.arkanoid;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class Paddle extends Entity {
  private final Image[][] images =  new Image[2][4];
  private int state;
  private int imageState;
  private int imageCooldown;

  public Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 4; j++) {
        images[i][j] = new Image(getClass().getResource("Paddle/paddle" + i + j + ".png").toExternalForm());
      }
    }
    imageState = 0;
    imageCooldown = 20;
    state = 0;
  }

  public void setState(int state) {
    this.state = state;
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
    imageView.setImage(images[state][imageState]);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void checkCollisionPaddle(Ball ball) {
    if (ball.getCircle().getBoundsInParent().intersects(this.getRectangle().getBoundsInParent())) {
      ball.deltaY *= -1;
      ball.getCircle().setLayoutY(ball.getCircle().getLayoutY() + 2 * ball.deltaY);
      Sound.playBouncePaddle();
    }
  }
}
