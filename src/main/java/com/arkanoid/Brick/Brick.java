package com.arkanoid.Brick;


import com.arkanoid.Core.Entity;
import com.arkanoid.PowerUp.PowerUpManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Brick extends Entity {
    private Image[] images = new Image[6];
    protected int hp; // The brick will be destroyed when it runs out of hp
    private int hitCooldown;

    // create brick with size + type
    public Brick(double x, double y, double w, double h, int type) {
        super(x, y, w, h);
        if (type <= 7) {
          hp = 1;
          for (int i = 0; i < 6; i++) {
            images[i] = new Image(
                getClass().getResource("/com/arkanoid/Brick/brick1" + type + ".png").toExternalForm());
          }
        } else if (type == 8) {
          hp = 3;
          for (int i = 0; i < 6; i++) {
            images[i] = new Image(getClass().getResource("/com/arkanoid/Brick/brick2" + i + ".png").toExternalForm());
          }
        } else {
          hp = Integer.MAX_VALUE;
          for (int i = 0; i < 6; i++) {
            images[i] = new Image(getClass().getResource("/com/arkanoid/Brick/brick3" + i + ".png").toExternalForm());
          }
        }
        imageView.setImage(images[0]);
        imageView.setX(this.getRectangle().getX());
        imageView.setY(this.getRectangle().getY());
    }

    //get hp
    public int getHP() {
        return this.hp;
    }

  public boolean checkCollisionBrick(Entity entity) {
    Rectangle rectangle = (Rectangle) shape;

    if (hitCooldown > 0) {
      hitCooldown--;
    }
    imageView.setImage(images[hitCooldown / 2]);

    if (entity.getShape().getBoundsInParent().intersects(rectangle.getBoundsInParent())) {

      if (entity.getShape() instanceof Circle circle) {

        if (PowerUpManager.powerUpState[0] == 0) {

          boolean rightBorder = circle.getLayoutX() >= ((rectangle.getX() + rectangle.getWidth()) - circle.getRadius());
          boolean leftBorder = circle.getLayoutX() <= (rectangle.getX() + circle.getRadius());
          boolean bottomBorder = circle.getLayoutY() >= ((rectangle.getY() + rectangle.getHeight()) - circle.getRadius());
          boolean topBorder = circle.getLayoutY() <= (rectangle.getY() + circle.getRadius());

          if (rightBorder || leftBorder) {
            entity.setDeltaX(entity.getDeltaX() * -1);
          }
          if (bottomBorder || topBorder) {
            entity.setDeltaY(entity.getDeltaY() * -1);
          }

        }
      }

      this.hp--;
      if (hitCooldown == 0) hitCooldown = 10;
      return true;
    }
    return false;
  }

}
