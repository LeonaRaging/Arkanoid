package com.arkanoid.Number_and_string_display;

import com.arkanoid.core.Entity;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Letter extends Entity {

    private Image image;

    public Letter(int x, int y, int w, int h, char letter) {
        super(x, y, w, h);
        image = new Image(
                getClass().getResource("/com/arkanoid/number_and_alphabet/blue" + letter + ".png")
                        .toExternalForm());
        imageView.setImage(image);
        imageView.setX(this.getRectangle().getX());
        imageView.setY(this.getRectangle().getY());
    }

    public void change(char newletter) {
        image = new Image(
                getClass().getResource("/com/arkanoid/number_and_alphabet/blue" + newletter + ".png")
                        .toExternalForm());
        imageView.setImage(image);
        imageView.setX(this.getRectangle().getX());
        imageView.setY(this.getRectangle().getY());
    }

    public void display(AnchorPane scene) {
        scene.getChildren().add(this.getImageView());
    }

    public void undisplay(AnchorPane scene) {
        scene.getChildren().remove(this.getImageView());
    }
}
