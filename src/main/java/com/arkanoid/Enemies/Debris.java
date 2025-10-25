package com.arkanoid.Enemies;

import com.arkanoid.Controller;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Debris extends Enemies {
    private final Image[] images = new Image[11];
    private double moveSpeed = 1;
    private int state;
    private int stateCooldown;

    Debris(double x, double y, double r) {
        super(x, y, r);
        state = 0;
        stateCooldown = 10;

        for (int i = 0; i < 11; i++) {
            images[i] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/Debris/Debris0" + i + ".png").toExternalForm());
        }

    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene){
        imageView.setImage(images[state]);
        imageView.setX(this.getCircle().getCenterX() - 10);
        imageView.setY(this.getCircle().getCenterY() - 19);

        stateCooldown--;
        if (stateCooldown <= 0) {
            state = (state + 1) % 11;
            stateCooldown = 10;
        }

        if (this.getCircle().getBoundsInParent().intersects(Controller.paddle.getRectangle().getBoundsInParent())) {
            EnemiesManager.setGameOver(true);
            return true;
        }
        this.getCircle().setCenterY(this.getCircle().getCenterY() + moveSpeed);
        return outScene();
    }
}
