package com.arkanoid.Enemies;

import com.arkanoid.Controller;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class BossLaser extends Enemies {
    private final Rectangle[] lasts = new Rectangle[40];
    private double moveSpeed;

    public BossLaser(double x, double y, double w, int type, AnchorPane scene) {
        super(x, y, w, 1);


        for (int i = 0; i < 40; i++) {
            lasts[i] = new Rectangle(x, y, w, 3);
            lasts[i].setFill(Color.DARKGOLDENROD);
            scene.getChildren().add(lasts[i]);
        }

        if (type == 0) { // From mouth
            moveSpeed = 1;
            deltaX = 0;
            deltaY = 1;
        }
        else {
            moveSpeed = 2;
            Rectangle rect = Controller.paddle.getRectangle();
            double X = x + w / 2;
            double Y = y;
            double fX = rect.getX() + rect.getWidth() / 2;
            double fY = rect.getY() + rect.getHeight() / 2;
            double dis = Math.sqrt(Math.pow(X - fX, 2) + Math.pow(Y - fY, 2));
            deltaX = (fX - X) / dis;
            deltaY = (fY - Y) / dis;
        }
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        for (int i = 0; i < 40; i++) {
            if (lasts[i].getBoundsInParent().intersects(
                    Controller.paddle.getShape().getBoundsInParent())) {
                EnemiesManager.setGameOver(true);
            }
        }

        for (int i = 39; i > 0; i--) {
            lasts[i].setX(lasts[i - 1].getX());
            lasts[i].setY(lasts[i - 1].getY());
        }
        lasts[0].setX(lasts[0].getX() + deltaX * moveSpeed);
        lasts[0].setY(lasts[0].getY() + deltaY * moveSpeed);
        Rectangle rect = Controller.field.getRectangle();
        if (lasts[39].getX() > rect.getX() + rect.getWidth()) {
            for (int i = 0; i < 40; i++) {
                scene.getChildren().remove(lasts[i]);
            }
            return true;
        }
        return false;
    }
}
