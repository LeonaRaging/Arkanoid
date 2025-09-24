package com.arkanoid;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Brick extends Entity {
    protected int hp; // The brick will be destroyed when it runs out of hp

    // create brick with size + type
    public Brick(double x, double y, double w, double h, int type) {
        super(x, y, w, h);
        switch (type) {
            case 1:
                hp = 1;
                break;
            case 2:
                hp = 3;
                break;
            case 3:
                hp = Integer.MAX_VALUE;
                break;
            default:
                hp = 1;
                break;
        }
    }

    //get hp
    public int getHP() {
        return this.hp;
    }

  public void checkCollisionBrick(Entity entity) {
    Rectangle rectangle = (Rectangle) shape;

    if (entity.getShape().getBoundsInParent().intersects(rectangle.getBoundsInParent())) {

      if (entity.getShape() instanceof Circle circle) {

        if (PowerUpManager.powerUpState[0] == 0) {

          boolean rightBorder = circle.getLayoutX() >= ((rectangle.getX() + rectangle.getWidth()) - circle.getRadius());
          boolean leftBorder = circle.getLayoutX() <= (rectangle.getX() + circle.getRadius());
          boolean bottomBorder = circle.getLayoutY() >= ((rectangle.getY() + rectangle.getHeight()) - circle.getRadius());
          boolean topBorder = circle.getLayoutY() <= (rectangle.getY() + circle.getRadius());

          if (rightBorder || leftBorder) {
            entity.deltaX *= -1;
          }
          if (bottomBorder || topBorder) {
            entity.deltaY *= -1;
          }

        }
      }

      this.hp--;
    }
  }
}
