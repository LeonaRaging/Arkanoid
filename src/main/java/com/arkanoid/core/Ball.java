package com.arkanoid.core;

import com.arkanoid.powerup.PowerUpManager;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Ball extends Entity {

  public class Pos {
    public double x;
    public double y;
    Pos(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public double distance(Pos o) {
      return Math.abs(x - o.x) + Math.abs(y - o.y);
    }
  }

  Deque<Pos> deque = new LinkedList<>();
  private Image[] images = new Image[4];
  private Image[] imagesPowerUp = new Image[3];
  public ImageView[] imageViews = new ImageView[6];
  public int ballType = 0;

  public Ball(double x, double y, double r) {
    super(x, y, r);
    images[0] = new Image(getClass().getResource("/com/arkanoid/ball/ball.png").toExternalForm());
    for (int i = 1; i < 4; i++) {
      images[i] = new Image(
          getClass().getResource("/com/arkanoid/ball/MMball0" + (i - 1) + ".png").toExternalForm());
    }
    for (int i = 0; i < 3; i++) {
      imagesPowerUp[i] = new Image(
          getClass().getResource("/com/arkanoid/ball/ballPowerUp" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 6; i++) {
      imageViews[i] = new ImageView();
      imageViews[i].setImage(imagesPowerUp[i / 2]);
    }
    imageView.setImage(images[0]);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  @Override
  public void update(AnchorPane scene) {
    imageView.setImage(images[ballType]);
    this.getShape().setLayoutX(this.getShape().getLayoutX() + deltaX);
    this.getShape().setLayoutY(this.getShape().getLayoutY() + deltaY);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);

    Pos pos = new Pos(this.getCircle().getLayoutX() - 2.5, this.getCircle().getLayoutY() - 2.5);
    if (!deque.isEmpty()) {
      if (pos.distance(deque.getFirst()) >= 2.5) {
        deque.addFirst(pos);
      }
    } else {
      deque.addFirst(pos);
    }
    if (deque.size() > 6) {
      deque.pollLast();
    }
    int i = 0;
    for (Pos newPos : deque) {
      imageViews[i].setX(newPos.x);
      imageViews[i].setY(newPos.y);
      i++;
    }
  }

  public void updateX(double x) {
    Random rand = new Random();
    deltaX = x * Math.abs(deltaX);
    deltaY += (rand.nextDouble() - 0.5) * 0.3;
    int sign = (deltaY >= 0) ? 1 : -1;
    double minSpeed = (PowerUpManager.powerUpState[0] == 1 ? 5 : 2);
    if (deltaX * deltaX + deltaY * deltaY < minSpeed) {
      double speed = Math.sqrt(minSpeed - deltaX * deltaX);
      deltaY = sign * speed;
    }
  }

  public void updateY(double y) {
    Random rand = new Random();
    deltaX += (rand.nextDouble() - 0.5) * 0.3;
    deltaY = y * Math.abs(deltaY);
    double minSpeed = (PowerUpManager.powerUpState[0] == 1 ? 5 : 2);
    if (deltaX * deltaX + deltaY * deltaY < minSpeed) {
      double speed = Math.sqrt(minSpeed - deltaX * deltaX);
      deltaY = y * speed;
    }
  }
}
