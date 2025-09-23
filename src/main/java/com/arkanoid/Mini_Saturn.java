package com.arkanoid;

public class Mini_Saturn extends Enemies {
    double TotalTime = 0;
    double BaseX = 0;
    public Mini_Saturn (double x, double y, double w, double h) {
        super(x, y, w, h);
        TotalTime = 0;
        BaseX = x;
    }
    @Override
    public void update(double DeltaTime) {
        double Amplitude = 50;
        double SwingSpeed = 2;
        double FallSpeed = 10;
        TotalTime =  TotalTime + DeltaTime;
        pos.setY(pos.getY() + FallSpeed * DeltaTime);
        pos.setX(BaseX + Amplitude * Math.sin(SwingSpeed * TotalTime));
    }
}