package com.arkanoid;


import javafx.scene.shape.Circle;

public class Brick extends Entity {
    protected int hp; // The brick will be destroyed when it runs out of hp

    // create brick with size
    public Brick(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

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

  public void checkCollisionBrick(Circle circle){

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
      this.hp--;
    }
  }
}
