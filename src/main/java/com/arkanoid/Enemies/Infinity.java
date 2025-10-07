package com.arkanoid.Enemies;

import com.arkanoid.Core.BallManager;
import com.arkanoid.Core.Ball;
import com.arkanoid.Sound.Sound;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Infinity extends Enemies {
    int MoveCooldown = 450;
    private final Image[][] images = new Image[3][12];
    private int state = 0;
    private int imageState;
    private int imageCooldown;

    public Infinity(double x, double y, double w, double h) {
        super(x, y, w, h);
        deltaX = 0.2;
        deltaY = 0;
        for (int i = 0; i < 4; i++)
            images[0][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Infinity/Infinity0" + i + ".png").toExternalForm());
        for (int i = 0; i < 12; i++)
            images[1][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/TriangleC/TriangleC1" + i + ".png").toExternalForm());
        for (int i = 0; i < 2; i++)
            images[2][i] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Infinity/Infinity2" + i + ".png").toExternalForm());
        state = 0;
        imageState = 0;
        imageCooldown = 20;
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        System.out.println(state);
        switch (state) {
            case 0:
                if (MoveCooldown == 0) {
                    deltaX = -deltaX;
                    MoveCooldown = 450;
                }

                if (this.checkCollisionBall()) {
                    state = 1;
                    imageCooldown = 20;
                    imageState = 0;
                    MoveCooldown = imageCooldown * 12;
                    Sound.playExplode();
                }
                else {
                    imageCooldown--;
                    if (imageCooldown == 0) {
                        imageState++;
                        imageState %= 4;
                        imageCooldown = 20;
                    }

                    this.getRectangle().setX(this.getRectangle().getX() + deltaX);
                    if(this.checkCollisionScene() || this.checkCollisionEnemy() || this.checkCollisionBrick())
                        this.getRectangle().setX(this.getRectangle().getX() - deltaX);
                    MoveCooldown--;
                    if (MoveCooldown == 299) {
                        state = 2;
                        imageCooldown = 20;
                        imageState = 0;
                        Sound.playInfinity();
                    }
                }
            break;
            case 1:
                imageCooldown--;
                MoveCooldown--;
                if (MoveCooldown == 0) return true;
                if (imageCooldown == 0) {
                    imageState++;
                    imageCooldown = 20;
                }
            break;
            case 2:
                Rectangle rectangle = new Rectangle(this.getRectangle().getX() - 6, this.getRectangle().getY() - 6,
                        this.getRectangle().getWidth() + 6, this.getRectangle().getHeight() + 6);
                for(Ball ball: BallManager.getBalls())
                    if (ball.getShape().getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                        Circle circle = ball.getCircle();
                        boolean rightBorder = circle.getLayoutX() >= ((rectangle.getX() + rectangle.getWidth()) - circle.getRadius());
                        boolean leftBorder = circle.getLayoutX() <= (rectangle.getX() + circle.getRadius());
                        boolean bottomBorder = circle.getLayoutY() >= ((rectangle.getY() + rectangle.getHeight()) - circle.getRadius());
                        boolean topBorder = circle.getLayoutY() <= (rectangle.getY() + circle.getRadius());

                        if (rightBorder || leftBorder) {
                            ball.setDeltaX(ball.getDeltaX() * -1);
                        }
                        if (bottomBorder || topBorder) {
                            ball.setDeltaY(ball.getDeltaY() * -1);
                        }
                    }
                imageCooldown--;
                MoveCooldown--;
                if (MoveCooldown <= 100) {
                    state = 0;
                    imageState = 0;
                    imageCooldown = 20;
                }
                if (imageCooldown == 0) {
                    imageState++;
                    imageState %= 2;
                    imageCooldown = 20;
                }
            break;
        }

        imageView.setImage(images[state][imageState]);
        imageView.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2 - images[state][imageState].getWidth() / 2);
        imageView.setY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2 - images[state][imageState].getHeight() / 2);

        return false;
    }
}
