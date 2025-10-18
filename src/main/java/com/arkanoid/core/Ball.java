package com.arkanoid.core;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Ball extends Entity {

  private Image[] images = new Image[4];
  public int ballType = 0;

  public Ball(double x, double y, double r) {
    super(x, y, r);
    images[0] = new Image(getClass().getResource("/com/arkanoid/ball/ball.png").toExternalForm());
    for (int i = 1; i < 4; i++) {
      images[i] = new Image(
          getClass().getResource("/com/arkanoid/ball/MMball0" + (i - 1) + ".png").toExternalForm());
    }
    imageView.setImage(images[0]);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  @Override
  public void update(AnchorPane scene) {
    imageView.setImage(images[ballType]);
    this.getShape().setLayoutX(this.getShape().getLayoutX() + deltaX);
    this.getShape().setLayoutY(this.getShape().getLayoutY() + deltaY);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  public void updateX(double x) {
    Random rand = new Random();
    deltaX = x * Math.abs(deltaX);
    deltaY += (rand.nextDouble() - 0.5) * 0.3;
    if (deltaY < 0.75 && deltaY >= 0) {
      deltaY = 0.75;
    }
    if (deltaY > -0.75 && deltaY < 0) {
      deltaY = -0.75;
    }
  }

  public void updateY(double y) {
    Random rand = new Random();
    deltaX += (rand.nextDouble() - 0.5) * 0.3;
    deltaY = y * Math.abs(deltaY);
    if (deltaX < 0.75 && deltaX >= 0) {
      deltaY = 0.75;
    }
    if (deltaX > -0.75 && deltaX < 0) {
      deltaY = -0.75;
    }
  }
}
