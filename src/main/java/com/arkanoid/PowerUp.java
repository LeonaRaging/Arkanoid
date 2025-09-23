package com.arkanoid;

import javafx.scene.layout.AnchorPane;

public class PowerUp extends Entity {
    private int type;
    final double speed = 2;

    public PowerUp(double x, double y, double w, double h, int type) {
      super(x, y, w, h);
      this.type = type;
    }

    public int getType() {
      return type;
    }

    @Override
    public void update(AnchorPane scene) {
      pos.setY(pos.getY() + speed);
    }
}
