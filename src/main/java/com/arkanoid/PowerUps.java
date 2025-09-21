package com.arkanoid;

import javafx.scene.layout.AnchorPane;

public class PowerUps extends Entity {
    private int type;
    final double speed = 2;

    public PowerUps(double x, double y, double w, double h) {
      super(x, y, w, h);
    }

    public int getType() {
      return type;
    }

    @Override
    public void update(AnchorPane scene) {
      pos.setY(pos.getY() + speed);
    }
}
