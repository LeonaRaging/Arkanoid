package com.arkanoid.enemies;

import com.arkanoid.Controller;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class MiniDohFace extends Enemies {
    private int type;
    private double targetX;
    private double targetY;
    private double focusX;
    private double focusY;
    private int deathCooldown;
    private int moveCooldown;
    private double speed = 0.2;
    private boolean isDeath = false;
    private boolean readyShoot;
    private final Image[][] images = new Image[2][8];
    private int state;

    public MiniDohFace(double x, double y, int type) {
        super(x, y, 14, 24);
        this.type = type;

        for (int i = 0; i < 2; i++) {
            images[0][i] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/MiniDohFace/MiniDohFace0" + i + ".png").toExternalForm());
        }

        for (int i = 0; i < 8; i++) {
            images[1][i] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/MiniDohFace/MiniDohFace1" + i + ".png").toExternalForm());
        }

        Rectangle rect = Controller.field.getRectangle();
        switch (this.type) {
            case 0: // Top left
                targetX = rect.getWidth() / 4;
                targetY = rect.getHeight() / 4 - 20;
                break;
            case 1: // Top right
                targetX = rect.getWidth() / 4 * 3;
                targetY = rect.getHeight() / 4 - 20;
                break;
            case 2: // Bottom left
                targetX = rect.getWidth() / 4 - 25;
                targetY = rect.getHeight() / 4 * 3 - 15;
                break;
            case 3: // Bottom right
                targetX = rect.getWidth() / 4 * 3 + 25;
                targetY = rect.getHeight() / 4 * 3 - 15;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        targetX = rect.getX() + targetX - this.getRectangle().getWidth() / 2;
        targetY = rect.getY() + targetY - this.getRectangle().getHeight() / 2;

        moveCooldown = 0;
    }

    public void setDeath() {
        isDeath = true;
        state = 0;
        deathCooldown = 12;
    }

    public boolean isReadyShoot() {
        return readyShoot;
    }

    public boolean move(int bossState) {
        if (isDeath) {
            deathCooldown--;
            if (deathCooldown <= 0) {
                state++;
                deathCooldown = 12;
                if (state > 7) {
                    return true;
                }
            }

            imageView.setImage(images[1][state]);
            imageView.setX(this.getRectangle().getX()
                    + this.getRectangle().getWidth() / 2
                    - imageView.getImage().getWidth() / 2);
            imageView.setY(this.getRectangle().getY()
                    + this.getRectangle().getHeight() / 2
                    - imageView.getImage().getHeight() / 2);

            return false;
        }
        else {
            imageView.setImage(images[0][(bossState == 4 ? 1 : 0)]);
            imageView.setX(this.getRectangle().getX());
            imageView.setY(this.getRectangle().getY());
        }

        for (Ball ball: BallManager.getBalls()) {
            if (this.getShape().getBoundsInParent().intersects(
                    ball.getShape().getBoundsInParent())) {
                setDeath();
                ball.setDeltaX(-ball.getDeltaX());
                ball.setDeltaY(-ball.getDeltaY());
            }
        }

        double X = this.getRectangle().getX();
        double Y = this.getRectangle().getY();
        double dis;

        switch (bossState) {
            case 2:
//                System.out.println("moving");

                if (moveCooldown <= 0) {
//                    System.out.println("moving");
                    Rectangle rect = Controller.field.getRectangle();
                    focusX = rect.getX() + Math.random() * (rect.getWidth() / 2
                           - this.getRectangle().getWidth());
                    focusY = rect.getY() + Math.random() * (rect.getHeight() / 2
                           - this.getRectangle().getHeight());

                    switch (type) {
                        case 0:
                            break;
                        case 1:
                            focusX += rect.getWidth() / 2;
                            System.out.println(focusX + " " + focusY);
                            break;
                        case 2:
                            focusY += rect.getHeight() / 2;
                            break;
                        case 3:
                            focusX += rect.getWidth() / 2;
                            focusY += rect.getHeight() / 2;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }

                    moveCooldown = 100;
                }

                dis = Math.sqrt(Math.pow((X - focusX), 2) + Math.pow((Y - focusY), 2));

//                    System.out.println(X + " " + Y + " " + dis);

                if (dis <= speed) {
                    this.getRectangle().setX(focusX);
                    this.getRectangle().setY(focusY);
                    moveCooldown = 0;
                }
                else {
                    this.getRectangle().setX(X + (focusX - X) / dis * speed);
                    this.getRectangle().setY(Y + (focusY - Y) / dis * speed);
                }

                moveCooldown--;

                break;
            case 3:
                moveCooldown--;
                readyShoot = false;

                dis = Math.sqrt(Math.pow((X - targetX), 2) + Math.pow((Y - targetY), 2));

                if (dis <= speed) {
                    this.getRectangle().setX(targetX);
                    this.getRectangle().setY(targetY);
                    readyShoot = true;
                }
                else {
                    this.getRectangle().setX(X + (targetX - X) / dis * speed);
                    this.getRectangle().setY(Y + (targetY - Y) / dis * speed);
                }
                break;
        }

        return false;
    }

}
