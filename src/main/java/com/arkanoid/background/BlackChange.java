package com.arkanoid.background;

import com.arkanoid.core.Entity;
import javafx.scene.image.Image;

public class BlackChange extends Entity {

  private Image[] opacityAsc = new Image[11];
  private int blackFrame;
  private int blackCooldown;
  private static final int BLACK_FRAME_DELAY = 10;

  public BlackChange(double x, double y, double w, double h) {
    // x = 0, y = 0, w = 256, h = 224
    super(x, y, w, h);
    int curOpacity = 0;
    for (int i = 0; i < 11; i++) {
      opacityAsc[i] = new Image(
          getClass().getResource("/com/arkanoid/Background/nextLevelChange/" + curOpacity + ".png")
              .toExternalForm());
      curOpacity += 10;
    }
    imageView.setVisible(false);
  }

  public void startAsc() {
    blackFrame = 0;
    blackCooldown = 20;
    imageView.setImage(opacityAsc[0]);
    imageView.setVisible(true);
    imageView.toFront();
  }

  public boolean updateAsc() {
    if (blackFrame >= opacityAsc.length) {
      return false;
    }

    blackCooldown--;
    if (blackCooldown <= 0) {
      imageView.setX(this.getRectangle().getX());
      imageView.setY(this.getRectangle().getY());
      imageView.setImage(opacityAsc[blackFrame]);
      blackFrame++;
      blackCooldown = BLACK_FRAME_DELAY;
    }
    return true;
  }

  public void newLevel() {
    imageView.setVisible(false);
  }
}
