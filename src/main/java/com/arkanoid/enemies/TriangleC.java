package com.arkanoid.enemies;

import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class TriangleC extends Enemies {

  private int moveCooldown;
  double amplitude = 25;
  double swingSpeed = 1.5;
  double fallSpeed = 10;
  double baseX = 0;
  private final Image[][] images = new Image[2][12];
  private int state = 0;
  private int imageState;
  private int imageCooldown;

  public TriangleC(double x, double y, double w, double h) {
    super(x, y, w, h);
    baseX = x;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < (i == 1 ? 12 : 8); j++) {
        images[i][j] = new Image(getClass().getResource(
            "/com/arkanoid/enemy/TriangleC/TriangleC" + i + j + ".png").toExternalForm());
      }
    }
    state = 0;
    imageState = 0;
    imageCooldown = 20;
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {
    switch (state) {
      case 0:

        totalTime = totalTime + deltaTime;

        double oldY = this.getRectangle().getY();
        this.getRectangle().setY(this.getRectangle().getY() + fallSpeed * deltaTime);
        if (this.checkCollisionEnemy()
            || this.checkCollisionScene()
            || this.checkCollisionBrick()) {
          this.getRectangle().setY(oldY);
        }

        double changeX = amplitude * Math.sin(swingSpeed * totalTime);
        double oldX = this.getRectangle().getX();
        this.getRectangle().setX(baseX + changeX);
        if (this.checkCollisionEnemy()
            || this.checkCollisionScene()
            || this.checkCollisionBrick()) {
          this.getRectangle().setX(oldX);
          totalTime = totalTime - deltaTime;
        }

        if (this.checkCollisionBall()) {
          state = 1;
          imageCooldown = 20;
          imageState = 0;
          moveCooldown = imageCooldown * 12;
        } else {
          if (oldX != this.getRectangle().getX() || oldY != this.getRectangle().getY()) {
            imageCooldown--;
            if (imageCooldown == 0) {
              imageState++;
              imageState %= 8;
              imageCooldown = 20;
            }
          }
        }
        break;
      case 1:
        imageCooldown--;
        moveCooldown--;
        if (moveCooldown == 0) {
          return true;
        }
        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 20;
        }
        break;
      default:
        break;
    }

    imageView.setImage(images[state][imageState]);
    imageView.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2
        - images[state][imageState].getWidth() / 2);
    imageView.setY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2
        - images[state][imageState].getHeight() / 2);
    return outScene();
  }
}