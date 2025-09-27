package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Entity {
  protected Shape shape;
  protected double deltaX;
  protected double deltaY;

  public Entity(double x, double y, double w, double h) {
    this.shape = new Rectangle(x, y, w, h);
  }

  public Entity(double x, double y, double r) {
    this.shape = new Circle(x, y, r);
  }

  public Shape getShape() {
    return shape;
  }

  public Rectangle getRectangle() {
    return (Rectangle) shape;
  }

  public Circle getCircle() {
    return (Circle) shape;
  }

  public void update(AnchorPane scene) {}

}
