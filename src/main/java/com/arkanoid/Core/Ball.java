package com.arkanoid.Core;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Ball extends Entity {
  private Image[] images = new Image[4];
  public int ballType = 0;

  public Ball(double x, double y, double r) {
    super(x, y, r);
    images[0] = new Image(getClass().getResource("/com/arkanoid/Ball/ball.png").toExternalForm());
    for(int i = 1; i < 4; i++)
        images[i] = new Image(getClass().getResource("/com/arkanoid/Ball/MMball0" + (i - 1) + ".png").toExternalForm());
    imageView.setImage(images[0]);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  @Override
  public void update(AnchorPane scene) {
//    System.out.println(this.getShape().boundsInParentProperty());
    //deltaX = deltaY = 0.01;
    imageView.setImage(images[ballType]);
    this.getShape().setLayoutX(this.getShape().getLayoutX() + deltaX);
    this.getShape().setLayoutY(this.getShape().getLayoutY() + deltaY);
    imageView.setX(this.getCircle().getLayoutX() - 2.5);
    imageView.setY(this.getCircle().getLayoutY() - 2.5);
  }

  public void updateX(double x) {
    Random rand = new Random();
    deltaX = x * Math.abs(deltaX);
    deltaY += (rand.nextDouble() - 0.5) * 0.3;
  }

  public void updateY(double y) {
    Random rand = new Random();
    deltaX += (rand.nextDouble() - 0.5) * 0.3;
    deltaY = y * Math.abs(deltaY);
  }
}
