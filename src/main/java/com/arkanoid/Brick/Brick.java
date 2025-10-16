package com.arkanoid.Brick;


import com.arkanoid.Core.Ball;
import com.arkanoid.Core.Entity;
import com.arkanoid.PowerUp.PowerUpManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Brick extends Entity {
    private Image[] images = new Image[6];
    protected int hp; // The brick will be destroyed when it runs out of hp
    private int hitCooldown;
    private int score;

    // create brick with size + type
    public Brick(double x, double y, double w, double h, int type) {
        super(x, y, w, h);
        if (type <= 7) {
          hp = 1;
          score = 50;
          for (int i = 0; i < 6; i++) {
            images[i] = new Image(
                getClass().getResource("/com/arkanoid/Brick/brick1" + type + ".png").toExternalForm());
          }
        } else if (type == 8) {
          hp = 3;
          score = 100;
          for (int i = 0; i < 6; i++) {
            images[i] = new Image(getClass().getResource("/com/arkanoid/Brick/brick2" + i + ".png").toExternalForm());
          }
        } else {
          hp = Integer.MAX_VALUE;
          score = 0;
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

    public int getScore(){
        return this.score;
    }

  public boolean checkCollisionBrick(Entity entity) {
    Rectangle rect = (Rectangle) shape;

    if (hitCooldown > 0) {
      hitCooldown--;
    }
    imageView.setImage(images[hitCooldown / 2]);

    if (entity.getShape().getBoundsInParent().intersects(rect.getBoundsInParent())) {

      if (entity instanceof Ball ball) {

        if (PowerUpManager.powerUpState[0] == 0 || hp > 3) {

          boolean rightBorder = ball.getCircle().getLayoutX() >= (rect.getX() + rect.getWidth() - ball.getCircle().getRadius());
          boolean leftBorder = ball.getCircle().getLayoutX() <= (rect.getX() + ball.getCircle().getRadius());
          boolean bottomBorder = ball.getCircle().getLayoutY() >= (rect.getY() + rect.getHeight() - ball.getCircle().getRadius());
          boolean topBorder = ball.getCircle().getLayoutY() <= (rect.getY() + ball.getCircle().getRadius());

          if (rightBorder || leftBorder) {
            ball.updateX(rightBorder ? 1 : -1);
          }
          if (bottomBorder || topBorder) {
            ball.updateY(bottomBorder ? 1 : -1);
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
