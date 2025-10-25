package com.arkanoid.Background;

import com.arkanoid.core.Entity;
import javafx.scene.image.Image;

public class BlackChange extends Entity {
    private Image[] opacityASC = new Image[11];
    private int BlackFrame;
    private int BlackCooldown;
    private static final int BLACK_FRAME_DELAY = 10;

    public BlackChange(double x, double y, double w, double h) {
        //x = 0, y = 0, w = 256, h = 224
        super(x, y, w, h);
        int curOpacity = 0;
        for (int i = 0; i < 11; i++) {
            opacityASC[i] = new Image(getClass().getResource("/com/arkanoid/Background/nextLevelChange/" + curOpacity + ".png")
                            .toExternalForm());
            curOpacity+=10;
        }
        imageView.setVisible(false);
    }

    public void startASC() {
        BlackFrame = 0;
        BlackCooldown = 20;
        imageView.setImage(opacityASC[0]);
        imageView.setVisible(true);
        imageView.toFront();
    }

    public boolean updateASC() {
        if (BlackFrame >= opacityASC.length) {
            return false;
        }

        BlackCooldown--;
        if (BlackCooldown <= 0) {
            imageView.setX(this.getRectangle().getX());
            imageView.setY(this.getRectangle().getY());
            imageView.setImage(opacityASC[BlackFrame]);
            BlackFrame++;
            BlackCooldown = BLACK_FRAME_DELAY;
        }
        return true;
    }

    public void newLevel() {
        imageView.setVisible(false);
    }
}
