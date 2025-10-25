package com.arkanoid.Enemies;

import com.arkanoid.Controller;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Laser extends Enemies {
    private final Image image;
    double moveSpeed = 1;

    Laser(double x, double y) {
        super(x, y, 4, 32);

        image = new Image(getClass().getResource(
                "/com/arkanoid/Enemy/Laser/Laser.png").toExternalForm());
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene){
        if (this.getShape().getBoundsInParent().intersects(Controller.paddle.getRectangle().getBoundsInParent())) {
            EnemiesManager.setGameOver(true);
            return true;
        }

        this.getRectangle().setY(this.getRectangle().getY() + moveSpeed);
        imageView.setImage(image);
        imageView.setX(this.getRectangle().getX());
        imageView.setY(this.getRectangle().getY());

        return outScene();
    }
}
