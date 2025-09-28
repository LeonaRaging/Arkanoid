package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Bubble extends Enemies {
    int remainingTime = 300;
    int state;

    public Bubble(double x, double y, double w, double h) {
        super(x, y, w, h);
        state = 0;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        switch(state) {
            case 0:
                remainingTime--;
                if (remainingTime == 0) return true;
                if (checkCollisionBall()) {
                    BallManager.getBalls().removeIf(ball -> {
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            scene.getChildren().remove(ball.getImageView());
                            return true;
                        }
                        return false;
                    });
                    BallManager.isCaught++;
                    state = 1;
                    remainingTime = 100;
                }
                break;
            case 1:
                remainingTime--;
                if (remainingTime == 0) {
                    state = 2;
                    remainingTime = 100;
                    Rectangle rect = Controller.field.getRectangle();
                    double x = Math.random() * (rect.getWidth() - this.getRectangle().getWidth()) + rect.getX();
                    double y = Math.random() * (rect.getHeight() / 2 - this.getRectangle().getHeight()) + rect.getY();
                    this.getRectangle().setX(x);
                    this.getRectangle().setY(y);
                    while(this.checkCollisionBrick()) {
                        x = Math.random() * (rect.getWidth() - this.getRectangle().getWidth()) + rect.getX();
                        y = Math.random() * (rect.getHeight() / 2 - this.getRectangle().getHeight()) + rect.getY();
                        this.getRectangle().setX(x);
                        this.getRectangle().setY(y);
                    }
                }
                break;
            case 2:
                remainingTime--;
                if (remainingTime == 0) {
                    Ball ball = new Ball(0, 0, 2.5);
                    ball.getCircle().setLayoutX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2);
                    ball.getCircle().setLayoutY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2);
                    ball.deltaX = 1;
                    ball.deltaY = -1;
                    BallManager.getBalls().add(ball);
                    scene.getChildren().add(ball.getImageView());
                    BallManager.isCaught--;
                    return true;
                }


        }
        return false;
    }
}
