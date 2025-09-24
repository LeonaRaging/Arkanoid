package com.arkanoid;

import javafx.scene.layout.AnchorPane;

public class Ball extends Entity {
  Ball(double x, double y, double r) {
    super(x, y, r);
  }

  @Override
  public void update(AnchorPane scene) {
    this.getShape().setLayoutX(this.getShape().getLayoutX() + deltaX);
    this.getShape().setLayoutY(this.getShape().getLayoutY() + deltaY);
  }
}
