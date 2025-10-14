package com.arkanoid.Enemies;

import com.arkanoid.Core.BallManager;
import com.arkanoid.Brick.Brick;
import com.arkanoid.Brick.BrickManager;
import com.arkanoid.Core.Ball;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class RedBlob extends Enemies {
    int TransformCooldown = 200;

    int MoveCooldown = 99;
    double MoveSpeed = 0.25;
    int lastDirectionX;

    private final Image[][] images = new Image[3][12];
    private int state = 0;
    private int imageState;
    private int imageCooldown;

    public RedBlob(double x, double y, double r) {
        super(x, y, r); // r * 2 = Brick's width
        deltaX = 0;
        deltaY = MoveSpeed;
        lastDirectionX = 1;
        for (int i = 0; i < 10; i++)
            images[0][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/RedBlob/RedBlob0" + i + ".png").toExternalForm());
        for (int i = 0; i < 12; i++)
            images[1][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/TriangleC/TriangleC1" + i + ".png").toExternalForm());
        for (int i = 0; i < 3; i++)
            images[2][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/RedBlob/RedBlob2" + i + ".png").toExternalForm());
        state = 0;
        imageState = 0;
        imageCooldown = 20;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        switch (state) {
            case 0:
                this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
                this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
                if (MoveCooldown == 0 || this.checkCollisionBrick() || this.checkCollisionScene()
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
                    state = 1;
                    imageCooldown = 20;
                    imageState = 0;
                    MoveCooldown = imageCooldown * 12;
                }
                else {
                    if (TransformCooldown <= 0 && imageState == 9 && imageCooldown == 1) {
                        state = 2;
                        imageCooldown = 20;
                        imageState = 0;
                        MoveCooldown = imageCooldown * 3;
                    }
                    else {
                        imageCooldown--;
                        if (imageCooldown == 0) {
                            imageState++;
                            imageState %= 10;
                            imageCooldown = 20;
                        }
                    }
                }
            break;
            case 1:
                imageCooldown--;
                MoveCooldown--;
                if (MoveCooldown == 0) return true;
                if (imageState < 7 && imageCooldown == 19) {
                    for (Ball ball : BallManager.getBalls())
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            ball.updateX(1);
                            ball.updateY(1);
                        }
                }
                if (imageCooldown == 0) {
                    imageState++;
                    imageCooldown = 20;
                }
            break;
            case 2: // transform
                if (this.checkCollisionBall()) {
                    state = 1;
                    imageCooldown = 20;
                    imageState = 0;
                    MoveCooldown = imageCooldown * 12;
                }
                else {
                    if (imageState == 3) {
                        Brick brick = new Brick(this.getCircle().getCenterX() - this.getCircle().getRadius(),
                                this.getCircle().getCenterY() - this.getCircle().getRadius(), 16, 8, 1);
                        BrickManager.brick_remain++;
                        scene.getChildren().add(brick.getImageView());
                        BrickManager.getBricks().add(brick);
                        return true;
                    }
                    imageCooldown--;
                    if (imageCooldown == 0) {
                        imageState++;
                        imageCooldown = 20;
                    }
                }
            break;
        }

        imageView.setImage(images[state][imageState]);
        imageView.setX(this.getCircle().getCenterX() - this.getCircle().getRadius());
        imageView.setY(this.getCircle().getCenterY() - this.getCircle().getRadius());

        return false;
    }
}
