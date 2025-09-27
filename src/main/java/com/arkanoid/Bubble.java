package com.arkanoid;

import javafx.scene.layout.AnchorPane;

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
                    BallManager.balls.removeIf(ball -> {
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            scene.getChildren().remove(ball.getShape());
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
                    double x = Math.random() * (scene.getWidth() - this.getRectangle().getWidth());
                    double y = Math.random() * (scene.getHeight() / 2 - this.getRectangle().getHeight());
                    this.getRectangle().setLayoutX(x);
                    this.getRectangle().setLayoutY(y);
                    while(this.checkCollisionBrick()) {
                        x = Math.random() * (scene.getWidth() - this.getRectangle().getWidth());
                        y = Math.random() * (scene.getHeight() - this.getRectangle().getHeight());
                        this.getRectangle().setLayoutX(x);
                        this.getRectangle().setLayoutY(y);
                    }
                }
                break;
            case 2:
                remainingTime--;
                if (remainingTime == 0) {
                    Ball ball = new Ball(0, 0, 5);
                    ball.getCircle().setLayoutX(this.getRectangle().getLayoutX() + 83);
                    ball.getCircle().setLayoutY(this.getRectangle().getLayoutY() + 65);
                    ball.deltaX = 2;
                    ball.deltaY = -2;
                    BallManager.balls.add(ball);
                    scene.getChildren().add(ball.getShape());
                    BallManager.isCaught--;
                    return true;
                }


        }
        return false;
    }
}
