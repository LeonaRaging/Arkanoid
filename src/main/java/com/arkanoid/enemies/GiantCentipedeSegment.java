package com.arkanoid.enemies;

import com.arkanoid.Controller;
import javafx.scene.image.Image;

public class GiantCentipedeSegment extends Enemies{
    private double eps = 0.5;
    private boolean lead = false;
    private double TotalTime = 0;
    private double BaseX = 90;
    private double BaseY = 80;
    private double speed;
    private boolean changed = false;

    private final Image[][] images = new Image[2][3];
    private int state = 0;
    private int imageState;

    GiantCentipedeSegment(double x, double y, double r, boolean lead, double speed){
        super(x,y,r); // r = 13
        this.lead = lead;
        this.speed = speed;

        if (lead){
            BaseX = x;
            BaseY = y;
        }

        for (int i = 0; i < 3; i++)
            images[0][i] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/GiantCentipedeBoss" +
                            "/GiantCentipedeBoss0" + i + ".png").toExternalForm());
        images[1][0] = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/GiantCentipedeBoss" +
                        "/GiantCentipedeBoss1" + 0 + ".png").toExternalForm());
    }

    public void changeState(int state, int imageState) {
        this.state = state;
        this.imageState = imageState;
        imageView.setImage(images[state][imageState]);
        imageView.setX(this.getCircle().getCenterX() - this.getCircle().getRadius());
        imageView.setY(this.getCircle().getCenterY() - this.getCircle().getRadius());
    }

    public void move(double fX, double fY, double DeltaTime, int bossState) {
        double A = 90;
        double B = 35;
        double omega = 1.2 * 1.4;
        TotalTime += DeltaTime;

        switch (bossState) {
            case 0:
                if (lead) {
                    if (!changed) {
                        changed = true;
                        TotalTime = 0;
                    }
                    this.getCircle().setCenterX(BaseX + A * Math.sin(omega * TotalTime));
                    this.getCircle().setCenterY(BaseY + B * Math.sin(omega * 2 * TotalTime));
                }
                else {
                    // A(X, Y)
                    // B(fX, fY)
                    // C(X, fY)
                    // D(fX, Y)
                    double X = this.getCircle().getCenterX();
                    double Y = this.getCircle().getCenterY();
                    double AB = Math.sqrt(Math.pow(X - fX, 2) + Math.pow(Y - fY, 2));
                    deltaX = fX - X;
                    deltaY = fY - Y;
                    this.getCircle().setCenterX(X + speed * deltaX);
                    this.getCircle().setCenterY(Y + speed * deltaY);
                }
            break;
            case 1:
                if (!lead) {
                    double nSpeed = 0.8;
                    double X = this.getCircle().getCenterX();
                    double Y = this.getCircle().getCenterY();
                    double AB = Math.sqrt(Math.pow(X - fX, 2) + Math.pow(Y - fY, 2));
                    if (AB > eps) {
                        deltaX = (fX - X) / AB;
                        deltaY = (fY - Y) / AB;
                        this.getCircle().setCenterX(X + nSpeed * deltaX);
                        this.getCircle().setCenterY(Y + nSpeed * deltaY);
                    }
                }
                else changed = false;
            break;
            case 2:
                if (lead) {
                    if (!changed) {
                        changed = true;
                        double X = this.getCircle().getCenterX();
                        double Y = this.getCircle().getCenterY();
                        double realfX = Controller.paddle.getRectangle().getX();
                        double realfY = Controller.paddle.getRectangle().getY();
                        double dis = Math.sqrt(Math.pow(X - realfX, 2) + Math.pow(Y - realfY, 2));
                        deltaX = (realfX - X) / dis;
                        deltaY = (realfY - Y) / dis;
                    }
                    //System.out.println(this.getCircle().getCenterX() + " " + this.getCircle().getCenterY());
                    double nSpeed = 4;
                    this.getCircle().setCenterX(this.getCircle().getCenterX() + nSpeed * deltaX);
                    this.getCircle().setCenterY(this.getCircle().getCenterY() + nSpeed * deltaY);
                }
                else {
                    double X = this.getCircle().getCenterX();
                    double Y = this.getCircle().getCenterY();
                    double dis = Math.sqrt(Math.pow(X - fX, 2) + Math.pow(Y - fY, 2));
                    if (dis > this.getCircle().getRadius() * 2 * 0.6) {
                        deltaX = (fX - X) / dis;
                        deltaY = (fY - Y) / dis;
                        double nSpeed = 4;
                        this.getCircle().setCenterX(X + nSpeed * deltaX);
                        this.getCircle().setCenterY(Y + nSpeed * deltaY);
                    }
                }
            break;
            case 4:
                if (lead) {
                    if (!changed) {
                        changed = true;
                        double X = this.getCircle().getCenterX();
                        double Y = this.getCircle().getCenterY();
                        double dis = Math.sqrt(Math.pow(X - BaseX, 2) + Math.pow(Y - BaseY, 2));
                        deltaX = (BaseX - X) / dis;
                        deltaY = (BaseY - Y) / dis;
                    }
                    //System.out.println("4: " + this.getCircle().getCenterX() + " " + this.getCircle().getCenterY());
                    double nSpeed = 1.5;
                    this.getCircle().setCenterX(this.getCircle().getCenterX() + nSpeed * deltaX);
                    this.getCircle().setCenterY(this.getCircle().getCenterY() + nSpeed * deltaY);
                }
                else {
                    double X = this.getCircle().getCenterX();
                    double Y = this.getCircle().getCenterY();
                    double dis = Math.sqrt(Math.pow(X - fX, 2) + Math.pow(Y - fY, 2));
                    if (dis > this.getCircle().getRadius() * 2 * 0.6) {
                        deltaX = (fX - X) / dis;
                        deltaY = (fY - Y) / dis;
                        double nSpeed = 1.5;
                        this.getCircle().setCenterX(X + nSpeed * deltaX);
                        this.getCircle().setCenterY(Y + nSpeed * deltaY);
                    }
                }
            break;
        }


    }

    public boolean getMid() {
        return Math.abs(this.getCircle().getCenterX() - BaseX) < 1
                && Math.abs(this.getCircle().getCenterY() - BaseY) < 1;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
