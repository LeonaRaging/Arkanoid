package com.arkanoid.core;

import com.arkanoid.Controller;
import com.arkanoid.sound.Sound;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class Paddle extends Entity {

  private final Image[][] images = new Image[2][4];

  private final Image[] appearImages = new Image[10];
  private int appearFrame;
  private int appearCooldown;
  private static final int APPEAR_FRAME_DELAY = 3;

  private final Image[] breakImages = new Image[15];
  private int breakFrame;
  private int breakCooldown;
  private static final int BREAK_FRAME_DELAY = 3;

  private int state;
  private int imageState;
  private int imageCooldown;

  public Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 4; j++) {
        images[i][j] = new Image(
            getClass().getResource("/com/arkanoid/paddle/paddle" + i + j + ".png")
                .toExternalForm());
      }
    }

    for (int i = 0; i < 10; i++) {
      String imagePath = "/com/arkanoid/paddle/paddleForm" + (i + 1) + ".png";
      appearImages[i] = new Image(getClass().getResource(imagePath).toExternalForm());
    }

    for (int i = 0; i < 15; i++) {
      String imagePath = "/com/arkanoid/paddle/paddleBreak" + i + ".png";
      breakImages[i] = new Image(getClass().getResource(imagePath).toExternalForm());
    }

    imageState = 0;
    imageCooldown = 20;
    state = 0;

    imageView.setVisible(false);
    resetAppearAnimation();
  }

  public void startBreakAnimation() {
    breakFrame = 0;
    breakCooldown = BREAK_FRAME_DELAY;
    imageView.setImage(breakImages[0]);
    imageView.setVisible(true);
  }

  public boolean updateBreakAnimation() {
    if (breakFrame >= breakImages.length) {
      imageView.setVisible(false);
      return false;
    }

    breakCooldown--;
    if (breakCooldown <= 0) {
      imageView.setX(this.getRectangle().getX());
      imageView.setY(this.getRectangle().getY()-5);
      imageView.setImage(breakImages[breakFrame]);
      breakFrame++;
      breakCooldown = BREAK_FRAME_DELAY;
    }
    return true;
  }

  public void resetAppearAnimation() {
    appearFrame = 0;
    appearCooldown = APPEAR_FRAME_DELAY;
    imageView.setVisible(false);
    imageView.setImage(appearImages[0]);
  }

  public boolean updateAppearAnimation() {
    if (!imageView.isVisible()) {
      imageView.setVisible(true);
    }

    if (appearFrame >= appearImages.length) {
      imageView.setX(this.getRectangle().getX());
      imageView.setY(this.getRectangle().getY());
      imageView.setImage(images[state][imageState]);
      return false;
    }

    appearCooldown--;
    if (appearCooldown <= 0) {
      imageView.setX(this.getRectangle().getX());
      imageView.setY(this.getRectangle().getY()-5);
      imageView.setImage(appearImages[appearFrame]);
      appearFrame++;
      appearCooldown = APPEAR_FRAME_DELAY;
    }

    return true;
  }

  public void setState(int state) {
    this.state = state;
  }

  public void update(Rectangle rect) {

    double xpos = this.getRectangle().getX();

    if (Controller.pressedKeys.contains(KeyCode.RIGHT)) {
      xpos += 2;
    }

    if (Controller.pressedKeys.contains(KeyCode.LEFT)) {
      xpos -= 2;
    }

    if (xpos < rect.getX()) {
      this.getRectangle().setX(rect.getX());
    } else if (xpos + this.getRectangle().getWidth() >= rect.getX() + rect.getWidth()) {
      this.getRectangle().setX(rect.getX() + rect.getWidth() - this.getRectangle().getWidth());
    } else {
      this.getRectangle().setX(xpos);
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
      double distance = ball.getCircle().getLayoutX() - (this.getRectangle().getX()
          + this.getRectangle().getWidth() / 2);
      ball.setDeltaX(distance / 10);
      ball.setDeltaY(-Math.abs(ball.getDeltaY()));
      Sound.playBouncePaddle();
    }
  }
}
