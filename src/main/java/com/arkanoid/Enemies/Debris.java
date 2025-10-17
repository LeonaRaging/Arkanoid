package com.arkanoid.Enemies;

import com.arkanoid.Controller;
import javafx.scene.layout.AnchorPane;

public class Debris extends Enemies {

    double moveSpeed = 1;

    Debris(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene){
        if (checkCollisionScene()) return true;
        if (this.getCircle().getBoundsInParent().intersects(Controller.paddle.getRectangle().getBoundsInParent())) {
            EnemiesManager.setGameOver(true);
            return true;
        }
        this.getCircle().setCenterY(this.getCircle().getCenterY() + moveSpeed);
        return false;
    }
}
