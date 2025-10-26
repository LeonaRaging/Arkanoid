package com.arkanoid.field;

import com.arkanoid.core.Entity;
import com.arkanoid.enemies.Enemies;
import com.arkanoid.enemies.EnemiesManager;
import javafx.scene.image.Image;

public class Gate extends Entity {

  private Image[] image = new Image[5];
  private int state;
  private int frame;

  public int getState() {
    return state;
  }

  public Gate(int x, int y, int w, int h, String name) {
    super(x, y, w, h);
    for (int i = 0; i < 5; i++) {
      image[i] = new Image(getClass()
          .getResource("/com/arkanoid/field/gate/" + name + i + ".png").toExternalForm());
      image[i] = new Image(getClass()
          .getResource("/com/arkanoid/field/gate/" + name + i + ".png").toExternalForm());
    }
    state = frame = 0;
    imageView.setImage(image[frame]);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void update(int gateIndex) {
    boolean isNear = false;
    if (state == 1) {
      frame++;
      if (frame == 39) {
        state = 3;
      }
    }
    if (state == 2) {
      frame--;
      if (frame == 0) {
        state = 0;
      }
    }
    for (Enemies enemy : EnemiesManager.getEnemies()) {
      switch (gateIndex) {
        case 0:
          if (enemy.isAbove(this.getRectangle())) {
            isNear = true;
          }
          break;
        case 1:
          if (enemy.isAbove(this.getRectangle())) {
            isNear = true;
          }
          break;
        case 2:
          if (enemy.isLeft(this.getRectangle())) {
            isNear = true;
          }
          break;
        case 3:
          if (enemy.isRight(this.getRectangle())) {
            isNear = true;
          }
          break;
        default:
          break;
      }
    }
    if (isNear) {
      if (state == 0) {
        state = 1;
      }
    } else {
      if (state == 3) {
        state = 2;
      }
    }
    imageView.setImage(image[frame / 10]);
  }
}
