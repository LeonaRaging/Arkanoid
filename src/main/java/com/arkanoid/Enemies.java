package com.arkanoid;

import javafx.scene.shape.Circle;

public class Enemies extends Entity {
    public Enemies(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
    public boolean TakeHit(Circle circle) {
        return circle.getBoundsInParent().intersects(shape.getBoundsInParent());
    }

    public void update(double DeltaTime) {

    }
}


