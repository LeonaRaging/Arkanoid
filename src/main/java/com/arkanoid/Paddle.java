package com.arkanoid;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;

public class Paddle extends Entity {

  Robot robot = new Robot();

  public Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
  }

  @Override
  public void update(AnchorPane scene) {
    Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
    double sceneXPos = bounds.getMinX();

    double xPos = robot.getMouseX();
    double paddleWidth = this.pos.getWidth();

    if (xPos >= sceneXPos + (paddleWidth / 2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      pos.setX(xPos - sceneXPos - (paddleWidth / 2));
    } else if (xPos < sceneXPos + (paddleWidth / 2)) {
      pos.setX(0);
    } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      pos.setX(scene.getWidth() - paddleWidth);
    }
  }

  public void checkCollisionPaddle(Circle circle) {
    if (circle.getBoundsInParent().intersects(pos.getBoundsInParent())) {
      Controller.deltaY *= -1;
    }
  }
}
