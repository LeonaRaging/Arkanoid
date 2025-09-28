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

    public boolean checkCollisionScene(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        if(this.getShape() instanceof Circle circle) {
            boolean rightBorder = circle.getCenterX() >= (bounds.getMaxX() - circle.getRadius());
            boolean leftBorder = circle.getCenterX() <= (bounds.getMinX() + circle.getRadius());
            boolean bottomBorder = circle.getCenterY() >= (bounds.getMaxY() - circle.getRadius());
            boolean topBorder = circle.getCenterY() <= (bounds.getMinY() + circle.getRadius());
            if (rightBorder || leftBorder || bottomBorder || topBorder) {
                return true;
            }
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


