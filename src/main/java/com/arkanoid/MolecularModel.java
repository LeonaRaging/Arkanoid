package com.arkanoid;

import javafx.scene.layout.AnchorPane;

public class MolecularModel extends Enemies {
    double MoveSpeed = 0.2;
    int MoveCooldown = 150;
    public MolecularModel(double x, double y, double r) {
        super(x, y, r);
        deltaX = MoveSpeed;
        deltaY = MoveSpeed;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        if (this.checkCollisionBall()) {
            // Ball 1 (-1, -1)
            Ball ball1 = new Ball(0, 0, 2.5);
            ball1.getCircle().setLayoutX(this.getCircle().getCenterX());
            ball1.getCircle().setLayoutY(this.getCircle().getCenterY());
            ball1.deltaX = -1;
            ball1.deltaY = -1;
            ball1.fromMM = true;
            BallManager.getBalls().add(ball1);
            scene.getChildren().add(ball1.getImageView());
            // Ball 2 (1, -1)
            Ball ball2 = new Ball(0, 0, 2.5);
            ball2.getCircle().setLayoutX(this.getCircle().getCenterX());
            ball2.getCircle().setLayoutY(this.getCircle().getCenterY());
            ball2.deltaX = 1;
            ball2.deltaY = -1;
            ball2.fromMM = true;
            BallManager.getBalls().add(ball2);
            scene.getChildren().add(ball2.getImageView());
            // Ball 3 (1, 1)
            Ball ball3 = new Ball(0, 0, 2.5);
            ball3.getCircle().setLayoutX(this.getCircle().getCenterX());
            ball3.getCircle().setLayoutY(this.getCircle().getCenterY());
            ball3.deltaX = 1;
            ball3.deltaY = 1;
            ball3.fromMM = true;
            BallManager.getBalls().add(ball3);
            scene.getChildren().add(ball3.getImageView());
            return true;
        }

        if (MoveCooldown == 0) {
            deltaX = -deltaX;
            MoveCooldown = 150;
        }
        else {
            this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
            if (this.checkCollisionScene() || this.checkCollisionBrick() || this.checkCollisionEnemy())
                this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
            this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
            if (this.checkCollisionScene() || this.checkCollisionBrick() || this.checkCollisionEnemy()) {
                this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
                this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
            }
            MoveCooldown--;
        }
        return false;
    }
}
