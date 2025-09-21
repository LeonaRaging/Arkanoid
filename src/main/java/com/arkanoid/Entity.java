package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Entity {
  protected Rectangle pos;

  public Entity(double x, double y, double w, double h) {
    this.pos = new Rectangle(x, y, w, h);
  }

  public Rectangle getPos() {
    return pos;
  }

  public void update(AnchorPane scene) {}

}
