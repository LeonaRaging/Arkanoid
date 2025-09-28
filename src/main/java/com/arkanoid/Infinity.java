package com.arkanoid;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Infinity extends Enemies {
    int MoveCooldown = 450;

    public Infinity(double x, double y, double w, double h) {
        super(x, y, w, h);
        deltaX = 0.2;
        deltaY = 0;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        if (MoveCooldown == 0) {
            deltaX = -deltaX;
            MoveCooldown = 450;
        }

        if(MoveCooldown <= 100 || MoveCooldown > 300) {
            this.getRectangle().setFill(Color.BLUE);
            if(this.checkCollisionBall())
                return true;
            this.getRectangle().setX(this.getRectangle().getX() + deltaX);
            if(this.checkCollisionScene() || this.checkCollisionEnemy() || this.checkCollisionBrick())
                this.getRectangle().setX(this.getRectangle().getX() - deltaX);
        }
        else {
            this.getRectangle().setFill(Color.CYAN);
            Rectangle rectangle = new  Rectangle(this.getRectangle().getX() - 5, this.getRectangle().getY() - 5,
                    this.getRectangle().getWidth() + 10, this.getRectangle().getHeight() + 10);
            for(Ball ball: BallManager.getBalls())
                if (ball.getShape().getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                   Circle circle = ball.getCircle();
                    boolean rightBorder = circle.getLayoutX() >= ((rectangle.getX() + rectangle.getWidth()) - circle.getRadius());
                    boolean leftBorder = circle.getLayoutX() <= (rectangle.getX() + circle.getRadius());
                    boolean bottomBorder = circle.getLayoutY() >= ((rectangle.getY() + rectangle.getHeight()) - circle.getRadius());
                    boolean topBorder = circle.getLayoutY() <= (rectangle.getY() + circle.getRadius());

                    if (rightBorder || leftBorder) {
                        ball.deltaX *= -1;
                    }
                    if (bottomBorder || topBorder) {
                        ball.deltaY *= -1;
                    }
                }
        }
        MoveCooldown--;
        return false;
    }
}
