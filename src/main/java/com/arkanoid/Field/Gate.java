package com.arkanoid.Field;

import com.arkanoid.Core.Entity;
import com.arkanoid.Enemies.Enemies;
import com.arkanoid.Enemies.EnemiesManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

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
      if (name == "gate0") {
        image[i] = new Image(getClass()
            .getResource("/com/arkanoid/Field/" + name + i + ".png").toExternalForm());
      } else {
        image[i] = new Image(getClass()
            .getResource("/com/arkanoid/Field/" + name + ".png").toExternalForm());
      }
    }
    state = frame = 0;
    imageView.setImage(image[frame]);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void update() {
    boolean isNear = false;
    if (state == 1) {
      frame++;
      if (frame == 39) state = 3;
    }
    if (state == 2) {
      frame--;
      if (frame == 0) state = 0;
    }
    for (Enemies enemy : EnemiesManager.getEnemies()) {
      if (enemy.isAbove(this.getRectangle())) {
        isNear = true;
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
