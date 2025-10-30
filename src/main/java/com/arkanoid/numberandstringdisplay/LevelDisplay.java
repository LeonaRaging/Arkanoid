package com.arkanoid.numberandstringdisplay;

import javafx.scene.layout.AnchorPane;

public class LevelDisplay {

  private Digit[] digitLevel = new Digit[2];
  int level;

  public LevelDisplay(int level) {
    this.level = level;
    digitLevel[1] = new Digit(232, 168, 8, 8, level % 10);
    digitLevel[0] = new Digit(224, 168, 8, 8, level / 10);
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
    digitLevel[1].change(level % 10);
    digitLevel[0].change(level / 10);
  }

  public void showLevel(AnchorPane scene) {
    for (int i = 0; i < 2; i++) {
      digitLevel[i].display(scene);
    }
  }

  public void clear(AnchorPane scene) {
    for (int i = 0; i < 2; i++) {
      digitLevel[i].undisplay(scene);
    }
  }
}
