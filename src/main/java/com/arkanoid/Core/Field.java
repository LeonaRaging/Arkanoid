package com.arkanoid.Core;

import javafx.scene.image.Image;

public class Field extends Entity {
  private Image image;
  public Field(int x, int y, int w, int h, String name) {
    super(x, y, w, h);
    image = new Image(getClass().getResource("/com/arkanoid/Field/" + name + ".png").toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }
}
