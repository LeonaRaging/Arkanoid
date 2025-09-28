package com.arkanoid;

import javafx.scene.layout.AnchorPane;

public class TriangleC extends Enemies {
    double Amplitude = 50;
    double SwingSpeed = 3;
    double FallSpeed = 20;
    double BaseX = 0;
    public TriangleC (double x, double y, double w, double h) {
        super(x, y, w, h);
        BaseX = x;
    }
    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {

        boolean bottomBorder = false;
        boolean leftBorder = false;
        boolean rightBorder = false;
        for(Brick brick : BrickManager.getBricks())
            if (brick.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                if (this.getRectangle().getY() + this.getRectangle().getHeight() >= brick.getRectangle().getY()) {
                    bottomBorder = true;
                }
                if (this.getRectangle().getX() <= brick.getRectangle().getX() +  brick.getRectangle().getWidth()
                && this.getRectangle().getX() >= brick.getRectangle().getX()) {
                    leftBorder = true;
                }
                if (this.getRectangle().getX() + this.getRectangle().getWidth() >= brick.getRectangle().getX()
                && this.getRectangle().getX() <= brick.getRectangle().getX()) {
                    rightBorder = true;
                }
            }


        TotalTime =  TotalTime + DeltaTime;
        if(!bottomBorder) {
            double oldY = this.getRectangle().getY();
            this.getRectangle().setY(this.getRectangle().getY() + FallSpeed * DeltaTime);
            if (this.checkCollisionEnemy()) {
                this.getRectangle().setY(oldY);
            }
        }

        double ChangeX = Amplitude * Math.sin(SwingSpeed * TotalTime);
        if((leftBorder && ChangeX < 0) || (rightBorder && ChangeX > 0)) {
            Amplitude = ChangeX = 0;
            BaseX = this.getRectangle().getX();
        }
        double oldX = this.getRectangle().getX();
        this.getRectangle().setX(BaseX + ChangeX);
        if(this.checkCollisionEnemy()) {
            this.getRectangle().setX(oldX);
            TotalTime = TotalTime - DeltaTime;
        }

        if (this.checkCollisionBall()) {
            return true;
        }


        return false;
    }
}