package com.arkanoid.PowerUp;

import com.arkanoid.Core.Entity;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class PowerUp extends Entity {
    private int type;
    private int imageCooldown;
    private Image[] images = new Image [8];
    final double speed = 1;

    public PowerUp(double x, double y, double w, double h, int type) {
      super(x, y, w, h);
      this.type = type;
      for (int i = 0; i < 8; i++) {
        images[i] = new Image (getClass().getResource("/com/arkanoid/Powerup/powerup" + type + i + ".png").toExternalForm());
      }
      imageCooldown = 0;
      imageView.setImage(images[imageCooldown]);
      imageView.setX(this.getRectangle().getX());
      imageView.setY(this.getRectangle().getY());
    }

    public int getType() {
      return type;
    }

    @Override
    public void update(AnchorPane scene) {
      this.getRectangle().setY(this.getRectangle().getY() + speed);
      imageCooldown++;
      imageCooldown %= 40;
      imageView.setImage(images[imageCooldown / 5]);
      imageView.setY(this.getRectangle().getY());
      imageView.setX(this.getRectangle().getX());
    }
}
