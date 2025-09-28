package com.arkanoid;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Enemies extends Entity {
    double TotalTime = 0;
    int order;
    int state;
    public Enemies(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
    public Enemies(double x, double y, double r) { super(x, y, r); }
    public boolean TakeHit(Circle circle) {
        return circle.getBoundsInParent().intersects(shape.getBoundsInParent());
    }

    public boolean checkCollisionBrick() {
        for (Brick brick: BrickManager.getBricks())
            if(brick.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent()))
                return true;
        return false;
    }

    public boolean checkCollisionBall() {
        for (Ball ball: BallManager.getBalls())
            if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                return true;
            }
        return false;
    }

    public boolean checkCollisionScene() {
        Rectangle rect = Controller.field.getRectangle();
        if (this.getShape() instanceof Rectangle rec) {
            return (rec.getX() + rec.getWidth() >= rect.getX() + rect.getWidth() ||
                    rec.getX() <= rect.getX() || rec.getY() <= rect.getY() ||
                    rec.getY() + rec.getHeight() >= rect.getY() + rect.getHeight());
        }
        if (this.getShape() instanceof Circle cir) {
            return (cir.getCenterX() + cir.getRadius() >= rect.getX() + rect.getWidth() ||
                    cir.getCenterX() - cir.getRadius() <= rect.getX() ||
                    cir.getCenterY() + cir.getRadius() >= rect.getY() + rect.getHeight() ||
                    cir.getCenterY() - cir.getRadius() <= rect.getY());
        }
        return false;
    }

    public boolean update(double DeltaTime, AnchorPane scene) {
        return true;
    }

    public boolean checkCollisionEnemy() {
        for (Enemies e: EnemiesManager.enemies)
            if (this != e && this.getShape().getBoundsInParent().intersects(e.getShape().getBoundsInParent()))
                return true;
        return false;
    }
}


