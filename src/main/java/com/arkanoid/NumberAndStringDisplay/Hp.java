package com.arkanoid.NumberAndStringDisplay;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Hp {

  private static int hp;
  private ImageView paddleIcon;
  private Digit[] showHp = new Digit[2];

  public Hp(AnchorPane scene) {
    Image lifeImage = new Image(
        getClass().getResource("/com/arkanoid/paddle/paddle00.png").toExternalForm());

    double startX = 200;
    final double ypos = 74;
    double iconWidth = 16;
    double spacing = 8;
    final double digitWidth = 8;
    double iconX = startX;
    double digitTensX = iconX + iconWidth + spacing;
    final double digitUnitsX = digitTensX + spacing;

    paddleIcon = new ImageView(lifeImage);
    paddleIcon.setFitWidth(iconWidth);
    paddleIcon.setPreserveRatio(true);
    paddleIcon.setX(iconX);
    paddleIcon.setY(ypos);

    int displayHp = (hp > 0) ? hp - 1 : 0;
    showHp[0] = new Digit((int) digitTensX, (int) ypos, (int) digitWidth, 8,
        displayHp / 10); // Hàng chục
    showHp[1] = new Digit((int) digitUnitsX, (int) ypos, (int) digitWidth, 8,
        displayHp % 10);  // Hàng đơn vị

    scene.getChildren().addAll(paddleIcon, showHp[0].getImageView(), showHp[1].getImageView());
  }

  public static void resetHp() {
    hp = 3;
  }

  public static void loseLife() {
    if (hp > 0) {
      hp--;
    }
  }

  public static int getHp() {
    return hp;
  }

  public void updateDisplay() {
    int displayHp = (hp > 0) ? hp - 1 : 0;
    int tens = displayHp / 10;
    int units = displayHp % 10;

    showHp[0].change(tens);
    showHp[1].change(units);

    boolean isVisible = hp > 0;
    paddleIcon.setVisible(isVisible);
    showHp[0].getImageView().setVisible(isVisible);
    showHp[1].getImageView().setVisible(isVisible);
  }

  public void clear(AnchorPane scene) {
    scene.getChildren().remove(paddleIcon);
    scene.getChildren().remove(showHp[0].getImageView());
    scene.getChildren().remove(showHp[1].getImageView());
  }
}
