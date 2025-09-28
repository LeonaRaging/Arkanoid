package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Red_Blob extends Enemies {
    int TransformCooldown = 500;

    int MoveCooldown = 99;
    double MoveSpeed = 0.5;
    int lastDirectionX;

    public Red_Blob(double x, double y, double r) {
        super(x, y, r);
        deltaX = 0;
        deltaY = MoveSpeed;
        lastDirectionX = 1;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        if (TransformCooldown == 0) {
            Brick brick = new Brick(this.getCircle().getCenterX() - 15,
                    this.getCircle().getCenterY() - 15, 30, 30, 1);
            BrickManager.brick_remain++;
            brick.getRectangle().setFill(Color.RED);
            scene.getChildren().add(brick.getRectangle());
            BrickManager.getBricks().add(brick);
            return true;
        }

        this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
        this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
        if (MoveCooldown == 0 || this.checkCollisionBrick() || this.checkCollisionScene(scene)
                || this.checkCollisionEnemy()) {
            this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
            this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
           if (deltaY > 0.0) { // down
               if (MoveCooldown == 99) { // -> up
                   deltaX = -MoveSpeed;
                   deltaY = 0;
               }
               else { // -> left/right
                   lastDirectionX *= -1;
                   deltaX = MoveSpeed * lastDirectionX;
                   deltaY = 0;
               }
               MoveCooldown = 100;
           }
           else if (deltaX < 0) { // left -> down
                deltaX = 0;
                deltaY = MoveSpeed;
                MoveCooldown = 100;
            }
           else if (deltaY < 0) { // up -> left/right
               lastDirectionX *= -1;
                deltaY = 0;
                deltaX = MoveSpeed * lastDirectionX;
                MoveCooldown = 100;
            }
           else if (deltaX > 0) { // right -> down
                deltaX = 0;
                deltaY = MoveSpeed;
                MoveCooldown = 100;
            }
        }
        MoveCooldown--;
        TransformCooldown--;

        if (this.checkCollisionBall()) {
            return true;
        }
        return false;
    }
}
