package com.arkanoid.brick;


import com.arkanoid.core.Ball;
import com.arkanoid.core.Entity;
import com.arkanoid.powerup.PowerUpManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Brick extends Entity {

  private Image[] images = new Image[6];
  private ImageView shadow = new ImageView();
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
            getClass()
                .getResource("/com/arkanoid/brick/brick1" + type + ".png").toExternalForm());
      }
    } else if (type == 8) {
      hp = 3;
      score = 100;
      for (int i = 0; i < 6; i++) {
        images[i] = new Image(getClass()
            .getResource("/com/arkanoid/brick/brick2" + i + ".png").toExternalForm());
      }
    } else {
      hp = Integer.MAX_VALUE;
      score = 0;
      for (int i = 0; i < 6; i++) {
        images[i] = new Image(getClass()
            .getResource("/com/arkanoid/brick/brick3" + i + ".png").toExternalForm());
      }
    }
    imageView.setImage(images[0]);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
    Image image = new Image(getClass()
        .getResource("/com/arkanoid/brick/shadow.png").toExternalForm());
    shadow.setImage(image);
    shadow.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2);
    shadow.setY(this.getRectangle().getY() + this.getRectangle().getHeight());
  }

  // get hp
  public int getHp() {
    return this.hp;
  }

  public int getScore() {
    return this.score;
  }

  public ImageView getShadow() {
    return this.shadow;
  }

  private void ballUpdate(Ball ball) {
    Rectangle rect = (Rectangle) shape;
    boolean rightBorder = ball.getCircle().getLayoutX()
        >= (rect.getX() + rect.getWidth() - ball.getCircle().getRadius());
    boolean leftBorder = ball.getCircle().getLayoutX()
        <= (rect.getX() + ball.getCircle().getRadius());
    boolean bottomBorder = ball.getCircle().getLayoutY()
        >= (rect.getY() + rect.getHeight() - ball.getCircle().getRadius());
    boolean topBorder = ball.getCircle().getLayoutY()
        <= (rect.getY() + ball.getCircle().getRadius());

    if (rightBorder || leftBorder) {
      ball.updateX(rightBorder ? 1 : -1);
    }
    if (bottomBorder || topBorder) {
      ball.updateY(bottomBorder ? 1 : -1);
    }
  }

  public boolean checkCollisionBrick(Entity entity) {
    Rectangle rect = (Rectangle) shape;

    if (hitCooldown > 0) {
      hitCooldown--;
    }
    imageView.setImage(images[hitCooldown / 2]);

    if (entity.getShape().getBoundsInParent().intersects(rect.getBoundsInParent())) {

      if (entity instanceof Ball ball && (PowerUpManager.powerUpState[0] == 0 || hp > 3)) {
        ballUpdate(ball);
      }

      this.hp--;
      if (hitCooldown == 0) {
        hitCooldown = 10;
      }
      return true;
    }
    return false;
  }

}
