package com.arkanoid.Enemies;

import com.arkanoid.Controller;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Firework extends Enemies {
    private Image[] images = new Image[8];
    private int state;
    private int stateCooldown;

    public Firework() {
        super(0, 0, 8);
        changePos();
        state = 0;
        stateCooldown = 10;

        for (int i = 0; i < 8; i++) {
            images[i] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/Firework/Firework0" + i + ".png").toExternalForm());
        }
    }

    public void changePos() {
        Rectangle rect = Controller.field.getRectangle();
        this.getCircle().setCenterX(rect.getX() + Math.random() * rect.getWidth());
        this.getCircle().setCenterY(rect.getY() + Math.random() * rect.getHeight());
        imageView.setX(this.getCircle().getCenterX() - 8);
        imageView.setY(this.getCircle().getCenterY() - 8);
    }

    public boolean move() {
        imageView.setImage(images[state]);
        stateCooldown--;
        if (stateCooldown <= 0) {
            state++;
            stateCooldown = 10;
        }
        return (state == 8);
    }
}
