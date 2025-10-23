package com.arkanoid.score;

import com.arkanoid.core.Entity;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Digit extends Entity {

  private Image image;

  public Digit(int x, int y, int w, int h, int num) {
    super(x, y, w, h);
    image = new Image(
        getClass().getResource("/com/arkanoid/number/" + num + ".png")
            .toExternalForm());
    imageView.setImage(image);
    imageView.setX(this.getRectangle().getX());
    imageView.setY(this.getRectangle().getY());
  }

  public void change(int newdigit) {
    image = new Image(
        getClass().getResource("/com/arkanoid/number/" + newdigit + ".png")
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
