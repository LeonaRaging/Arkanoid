package com.arkanoid;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Enemies extends Entity {
    public Enemies(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
    public boolean TakeHit(Circle circle) {
        return circle.getBoundsInParent().intersects(shape.getBoundsInParent());
    }


    public boolean update(double DeltaTime, ArrayList<Ball> balls,  ArrayList<Brick> bricks) {
        return true;
    }
}


