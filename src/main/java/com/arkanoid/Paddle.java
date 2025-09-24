package com.arkanoid;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.robot.Robot;

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
    double paddleWidth = this.getRectangle().getWidth();

    if (xPos >= sceneXPos + (paddleWidth / 2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      this.getRectangle().setX(xPos - sceneXPos - (paddleWidth / 2));
    } else if (xPos < sceneXPos + (paddleWidth / 2)) {
      this.getRectangle().setX(0);
    } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
      this.getRectangle().setX(scene.getWidth() - paddleWidth);
    }
  }

  public void checkCollisionPaddle(Ball ball) {
    if (ball.getCircle().getBoundsInParent().intersects(this.getRectangle().getBoundsInParent())) {
      ball.deltaY *= -1;
    }
  }
}
