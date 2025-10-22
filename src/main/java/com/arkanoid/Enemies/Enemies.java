package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Enemies extends Entity {

  double totalTime = 0;

  public Enemies(double x, double y, double w, double h) {
    super(x, y, w, h);
  }

  public Enemies(double x, double y, double r) {
    super(x, y, r);
  }

  public boolean takeHit(Circle circle) {
    return circle.getBoundsInParent().intersects(shape.getBoundsInParent());
  }

  public boolean checkCollisionBrick() {
    for (Brick brick : BrickManager.getBricks()) {
      if (brick.getShape().getBoundsInParent()
          .intersects(this.getShape().getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }

  public boolean checkCollisionBall() {
    for (Ball ball : BallManager.getBalls()) {
      if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }

  public boolean isAbove(Rectangle rect) {
    if (this.getShape() instanceof Rectangle rec) {
      if (rec.getX() >= rect.getX()
          && rec.getX() + rec.getWidth() <= rect.getX() + rect.getWidth()
          && rec.getY() <= rect.getY() + rect.getHeight()) {
        return true;
      }
    }
    if (this.getShape() instanceof Circle cir) {
      if (cir.getCenterX() - cir.getRadius() >= rect.getX()
          && cir.getCenterX() + cir.getRadius() <= rect.getX() + rect.getWidth()
          && cir.getCenterY() - cir.getRadius() <= rect.getY() + rect.getHeight()) {
        return true;
      }
    }
    return false;
  }

  public boolean isLeft(Rectangle rect) {
    if (this.getShape() instanceof Rectangle rec) {
      if (rec.getY() >= rect.getY()
          && rec.getY() + rec.getHeight() <= rect.getY() + rect.getHeight()
          && rec.getX() <= rect.getX() + rect.getWidth()) {
        return true;
      }
    }
    if (this.getShape() instanceof Circle cir) {
      if (cir.getCenterY() - cir.getRadius() >= rect.getY()
          && cir.getCenterY() + cir.getRadius() <= rect.getY() + rect.getHeight()
          && cir.getCenterX() - cir.getRadius() <= rect.getX() + rect.getWidth()) {
        return true;
      }
    }
    return false;
  }

  public boolean isRight(Rectangle rect) {
    if (this.getShape() instanceof Rectangle rec) {
      if (rec.getY() >= rect.getY()
          && rec.getY() + rec.getHeight() <= rect.getY() + rect.getHeight()
          && rec.getX() + rec.getWidth() >= rect.getX()) {
        return true;
      }
    }
    if (this.getShape() instanceof Circle cir) {
      if (cir.getCenterY() - cir.getRadius() >= rect.getY()
          && cir.getCenterY() + cir.getRadius() <= rect.getY() + rect.getHeight()
          && cir.getCenterX() - cir.getRadius() >= rect.getX() + rect.getWidth()) {
        return true;
      }
    }
    return false;
  }

  public boolean checkCollisionScene() {
    for (int i = 0; i < 2; i++) {
      if (Controller.gates[i].getState() == 3) {
        if (isAbove(Controller.gates[i].getRectangle())) {
          return false;
        }
      }
    }
    if (Controller.gates[2].getState() == 3) {
      if (isLeft(Controller.gates[2].getRectangle())) {
        return false;
      }
    }
    if (Controller.gates[3].getState() == 3) {
      if (isRight(Controller.gates[3].getRectangle())) {
        return false;
      }
    }
    Rectangle rect = Controller.field.getRectangle();
    if (this.getShape() instanceof Rectangle rec) {
      return (rec.getX() + rec.getWidth() >= rect.getX() + rect.getWidth()
          || rec.getX() <= rect.getX() || rec.getY() <= rect.getY()
          || rec.getY() + rec.getHeight() >= rect.getY() + rect.getHeight());
    }
    if (this.getShape() instanceof Circle cir) {
      return (cir.getCenterX() + cir.getRadius() >= rect.getX() + rect.getWidth()
          || cir.getCenterX() - cir.getRadius() <= rect.getX()
          || cir.getCenterY() + cir.getRadius() >= rect.getY() + rect.getHeight()
          || cir.getCenterY() - cir.getRadius() <= rect.getY());
    }
    return false;
  }

  public boolean update(double deltaTime, AnchorPane scene) {
    return true;
  }

  public boolean checkCollisionEnemy() {
    for (Enemies e : EnemiesManager.getEnemies()) {
      if (this != e && this.getShape().getBoundsInParent()
          .intersects(e.getShape().getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }
}


