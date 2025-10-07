package com.arkanoid.Enemies;

import com.arkanoid.Core.BallManager;
import com.arkanoid.Controller;
import com.arkanoid.Core.Ball;
import com.arkanoid.Sound.Sound;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Bubble extends Enemies {
    int remainingTime;
    private final Image[][] images = new Image[4][12];
    private int state = 0;
    private int imageState;
    private int imageCooldown;

    public Bubble(double x, double y, double w, double h) {
        super(x, y, w, h);
        for (int i = 0; i < 4; i++)
            images[0][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Bubble/Bubble0" + i + ".png").toExternalForm());
        for (int i = 0; i < 12; i++)
            images[1][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/TriangleC/TriangleC1" + i + ".png").toExternalForm());
        for (int i = 0; i < 2; i++)
            images[2][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Bubble/Bubble2" + i + ".png").toExternalForm());
        for (int i = 0; i < 6; i++)
            images[3][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Bubble/Bubble3" + i + ".png").toExternalForm());
        state = 0;
        imageState = 0;
        imageCooldown = 20;
        remainingTime = imageCooldown * 4;
        Sound.playBubble();
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        System.out.println("Bubble state: " + state);
        switch(state) {
            case 0: // spawn
                imageCooldown--;
                remainingTime--;
                if (imageCooldown == 0) {
                   imageState++;
                   imageCooldown = 20;
                }

                if (remainingTime == 0) {
                    state = 2;
                    remainingTime = 300;
                    imageState = 0;
                    imageCooldown = 20;
                }
            break;
            case 1: // explode
            break;
            case 2: // exist
                remainingTime--;
                imageCooldown--;
                if (imageCooldown == 0) {
                    imageState++;
                    imageState %= 2;
                    imageCooldown = 20;
                }

                if (checkCollisionBall()) {
                    BallManager.getBalls().removeIf(ball -> {
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            scene.getChildren().remove(ball.getImageView());
                            return true;
                        }
                        return false;
                    });
                    BallManager.isCaught++;

                    state = 3;
                    imageState = 0;
                    imageCooldown = 20;
                    remainingTime = imageCooldown * 6 * 2;
                }

                if (remainingTime == 0) {
                    Sound.playBubble();
                    state = 7;
                    imageState = 3;
                    imageCooldown = 20;
                    remainingTime = imageCooldown * 4;
                }
            break;
            case 3: // catching
                remainingTime--;
                imageCooldown--;
                if (imageCooldown == 19) {
                    for (Ball ball : BallManager.getBalls())
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            ball.setDeltaX(ball.getDeltaX() * -1);
                            ball.setDeltaY(ball.getDeltaY() * -1);
                        }
                }

                if (imageCooldown == 0) {
                    imageState++;
                    imageState %= 6;
                    imageCooldown = 20;
                }

                if (remainingTime == 0) {
                    Sound.playBubble();
                    state = 4;
                    imageState = 3;
                    imageCooldown = 20;
                    remainingTime = imageCooldown * 4 + 150;
                }
            break;
            case 4: // vanish while catching
                remainingTime--;
                imageCooldown--;
                if (imageCooldown == 0) {
                    if (imageState > 0) {
                        imageState--;
                        imageCooldown = 20;
                    }
                    else {
                        scene.getChildren().remove(this.getImageView());
                    }
                }

                if (remainingTime == 0) {
                    Sound.playBubble();
                    state = 5;
                    imageState = 0;
                    imageCooldown = 20;
                    teleport();
                    scene.getChildren().add(this.getImageView());
                }
            break;
            case 5: // spawn while catching
                imageCooldown--;
                remainingTime--;
                if (imageCooldown == 0) {
                    imageState++;
                    imageCooldown = 20;
                }

                if (remainingTime == 0) {
                    state = 6;
                    imageState = 0;
                    imageCooldown = 20;
                    remainingTime = imageCooldown * 6 * 2;
                }
            break;
            case 6: // exist while catching
                remainingTime--;
                imageCooldown--;
                if (imageCooldown == 0) {
                    imageState++;
                    imageState %= 6;
                    imageCooldown = 20;
                }

                if (imageCooldown == 19) {
                    for (Ball ball : BallManager.getBalls())
                        if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                            ball.setDeltaX(ball.getDeltaX() * -1);
                            ball.setDeltaY(ball.getDeltaY() * -1);
                        }
                }

                if (remainingTime == 0) {
                    Sound.playBubble();
                    state = 7;
                    imageState = 3;
                    imageCooldown = 20;
                    remainingTime = imageCooldown * 4;

                    Ball ball = new Ball(0, 0, 2.5);
                    ball.getCircle().setLayoutX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2);
                    ball.getCircle().setLayoutY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2);
                    ball.setDeltaX(-1);
                    ball.setDeltaY(-1);
                    BallManager.getBalls().add(ball);
                    scene.getChildren().add(ball.getImageView());
                    BallManager.isCaught--;
                }
            break;
            case 7: // disappear
                remainingTime--;
                imageCooldown--;
                if (imageCooldown == 0) {
                    imageState--;
                    imageCooldown = 20;
                }

                if (remainingTime == 0) {
                    Sound.playBubble();
                    state = 0;
                    imageState = 0;
                    imageCooldown = 20;
                    teleport();
                }
            break;
        }
        
        int imageDisplay = 0;
        switch (state) {
            case 0:
                imageDisplay = 0;
            break;
            case 1:
                imageDisplay = 1;
            break;
            case 2:
                imageDisplay = 2;
            break;
            case 3:
                imageDisplay = 3;
            break;
            case 4:
                imageDisplay = 0;
            break;
            case 5:
                imageDisplay = 0;
            break;
            case 6:
                imageDisplay = 3;
            break;
            case 7:
                imageDisplay = 0;
            break;
        }
        
        imageView.setImage(images[imageDisplay][imageState]);
        imageView.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2 - images[imageDisplay][imageState].getWidth() / 2);
        imageView.setY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2 - images[imageDisplay][imageState].getHeight() / 2);

        return false;
    }

    private void teleport() {
        remainingTime = imageCooldown * 4;
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
}
