package com.arkanoid;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Ball extends Entity {
  private Image image;
  public boolean fromMM = false;

  Ball(double x, double y, double r) {
    super(x, y, r);
    image = new Image(getClass().getResource("Ball/ball.png").toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  @Override
  public void update(AnchorPane scene) {
    this.getShape().setLayoutX(this.getShape().getLayoutX() + deltaX);
    this.getShape().setLayoutY(this.getShape().getLayoutY() + deltaY);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }
}
