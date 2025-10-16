package com.arkanoid.Field;

import com.arkanoid.Core.Entity;
import javafx.scene.image.Image;

public class Field extends Entity {
  private Image image;
  private int state;
  public Field(int x, int y, int w, int h, String name) {
    super(x, y, w, h);
    image = new Image(getClass().getResource("/com/arkanoid/Field/" + name + ".png").toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }
}
