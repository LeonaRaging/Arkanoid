package com.arkanoid;

import java.util.ArrayList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TriangleC extends Enemies {
    double Amplitude = 50;
    double SwingSpeed = 3;
    double FallSpeed = 20;
    double TotalTime = 0;
    double BaseX = 0;
    public TriangleC (double x, double y, double w, double h) {
        super(x, y, w, h);
        TotalTime = 0;
        BaseX = x;
    }
    @Override
    public boolean update(double DeltaTime, ArrayList<Ball> balls, ArrayList<Brick> bricks) {

        boolean bottomBorder = false;
        boolean leftBorder = false;
        boolean rightBorder = false;
        for(Brick brick : bricks)
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
        if(!bottomBorder) this.getRectangle().setY(this.getRectangle().getY() + FallSpeed * DeltaTime);
        double ChangeX = Amplitude * Math.sin(SwingSpeed * TotalTime);
        if((leftBorder && ChangeX < 0) || (rightBorder && ChangeX > 0)) {
            Amplitude = ChangeX = 0;
            BaseX = this.getRectangle().getX();
        }
        this.getRectangle().setX(BaseX + ChangeX);


        for (Ball ball: balls)
            if (ball.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
               return true;
            }


        return false;
    }
}