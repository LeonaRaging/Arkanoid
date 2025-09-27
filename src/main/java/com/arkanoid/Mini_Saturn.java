package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Mini_Saturn extends Enemies {
    private int MoveCooldown = 0;
    public Mini_Saturn(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        if (MoveCooldown == 0){
            MoveCooldown = (int)(Math.random() * 300) + 100;
        }

        if(MoveCooldown == 100){
            deltaX = (Math.random() - 0.5) * 2.5;
            deltaY = (Math.random() - 0.5) * 2.5;
        }

        if (MoveCooldown <= 100){
            this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
            this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
            if (this.checkCollisionBrick() || this.checkCollisionScene(scene) || this.checkCollisionEnemy()) {
                this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
                this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
                MoveCooldown = 1;
            }
        }

        MoveCooldown--;

        if (this.checkCollisionBall()){
            this.getCircle().setRadius(this.getCircle().getRadius() * 3);
            BrickManager.bricks.removeIf(brick->{
                if (this.getCircle().getBoundsInParent().intersects(brick.getShape().getBoundsInParent())){
                    BrickManager.brick_remain--;
                    scene.getChildren().remove(brick.getShape());
                    PowerUpManager.createPowerUps(brick, scene);
                    return true;
                }
                return false;
            });
            return true;
        }
        return false;
    }


}
