package com.arkanoid;


import javafx.scene.shape.Circle;

public class Brick extends Entity {
  public Brick(double x, double y, double w, double h) {
    super(x, y, w, h);
  }

  public boolean checkCollisionBrick(Circle circle){

    if(circle.getBoundsInParent().intersects(pos.getBoundsInParent())){
      boolean rightBorder = circle.getLayoutX() >= ((pos.getX() + pos.getWidth()) - circle.getRadius());
      boolean leftBorder = circle.getLayoutX() <= (pos.getX() + circle.getRadius());
      boolean bottomBorder = circle.getLayoutY() >= ((pos.getY() + pos.getHeight()) - circle.getRadius());
      boolean topBorder = circle.getLayoutY() <= (pos.getY() + circle.getRadius());

      if (rightBorder || leftBorder) {
        Controller.deltaX *= -1;
      }
      if (bottomBorder || topBorder) {
        Controller.deltaY *= -1;
      }

      return true;
    }
    return false;
  }
}
