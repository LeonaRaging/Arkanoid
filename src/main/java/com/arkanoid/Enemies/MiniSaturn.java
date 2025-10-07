package com.arkanoid.Enemies;

import com.arkanoid.Core.BallManager;
import com.arkanoid.Brick.BrickManager;
import com.arkanoid.Core.Ball;
import com.arkanoid.PowerUp.PowerUpManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;

public class MiniSaturn extends Enemies {
    private int MoveCooldown = 0;
    private final Image[][] images = new Image[2][6];
    private int state = 0;
    private int imageState;
    private int imageCooldown;

    public MiniSaturn(double x, double y, double r) {
        super(x, y, r);
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6 - i; j++)
                images[i][j] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/MiniSaturn/MiniSaturn" + i + j +".png").toExternalForm());
        state = 0;
        imageState = 0;
        imageView.setImage(images[state][imageState]);
        imageCooldown = 20;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        switch (state) {
            case 0:
                if (MoveCooldown == 0){
                    MoveCooldown = (int)(Math.random() * 300) + 100;
                }

                if(MoveCooldown == 100){
                    deltaX = (Math.random() - 0.5) * 1.2;
                    deltaY = (Math.random() - 0.5) * 1.2;
                    deltaX = deltaY = 0;
                }

                if (MoveCooldown <= 100){
                    this.getCircle().setCenterX(this.getCircle().getCenterX() + deltaX);
                    this.getCircle().setCenterY(this.getCircle().getCenterY() + deltaY);
                    if (this.checkCollisionBrick() || this.checkCollisionScene() || this.checkCollisionEnemy()) {
                        this.getCircle().setCenterX(this.getCircle().getCenterX() - deltaX);
                        this.getCircle().setCenterY(this.getCircle().getCenterY() - deltaY);
                        MoveCooldown = 1;
                    }
                }

                MoveCooldown--;

                if (this.checkCollisionBall()){
                    state = 1;
                    imageCooldown = 20;
                    imageState = 0;
                    MoveCooldown = imageCooldown * 5;
                }
                else {
                    imageCooldown--;
                    if (imageCooldown == 0) {
                        imageState++;
                        imageState %= 6;
                        imageCooldown = 20;
                    }
                }
            break;
            case 1:
                imageCooldown--;
                MoveCooldown--;
                if(MoveCooldown == 0) return true;
                if (imageCooldown == 0) {
                    imageState++;
                    imageCooldown = 20;
                }
                if (imageCooldown == 19) {
                    if (imageState == 0) {
                        this.getCircle().setRadius(this.getCircle().getRadius() * 2);
                        BrickManager.getBricks().removeIf(brick->{
                            if (this.getCircle().getBoundsInParent().intersects(brick.getShape().getBoundsInParent())){
                                BrickManager.brick_remain--;
                                scene.getChildren().remove(brick.getImageView());
                                PowerUpManager.createPowerUps(brick, scene);
                                return true;
                            }
                            return false;
                        });

                    }
                    for (Ball ball : BallManager.getBalls())
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            ball.setDeltaX(ball.getDeltaX() * -1);
                            ball.setDeltaY(ball.getDeltaY() * -1);
                        }
                }
            break;
        }

        imageView.setImage(images[state][imageState]);
        imageView.setX(this.getCircle().getCenterX() - images[state][imageState].getWidth() / 2);
        imageView.setY(this.getCircle().getCenterY() - images[state][imageState].getHeight() / 2);


        return false;
    }


}
