package com.arkanoid.field;

import com.arkanoid.core.Entity;
import javafx.scene.image.Image;

public class Field extends Entity {
  private Image image;
  private int state;

  public Field(int x, int y, int w, int h, String name) {
    super(x, y, w, h);
    image = new Image(getClass()
        .getResource("/com/arkanoid/field/" + name + ".png").toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void changeField(int level) {
    image = new Image(getClass()
        .getResource("/com/arkanoid/field/" + level + ".png").toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }
}
