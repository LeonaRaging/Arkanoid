package com.arkanoid.numberandstringdisplay;

import javafx.scene.layout.AnchorPane;

public class ScoreDisplay {

  private int score; // max <1e6
  private Digit[] digit = new Digit[6];

  public ScoreDisplay(int startX, int y, int score) {
    this.score = score;
    int width = 8;
    int height = 8;
    int spacing = 8;
    int copyscore = score;
    for (int i = 0; i < 6; i++) {
      digit[i] = new Digit(startX + i * spacing, y, width, height, 0);
    }
    for (int i = 0; i < 6; i++) {
      digit[5 - i].change(copyscore % 10);
      copyscore /= 10;
    }
  }

  public void reup() {
    int copyscore = score;
    for (int i = 0; i < 6; i++) {
      digit[5 - i].change(copyscore % 10);
      copyscore /= 10;
    }
  }

  public int getScore() {
    return score;
  }

  public void addScore(int points) {
    score += points;
  }

  public void setScore(int newScore) {
    score = newScore;
  }

  public void showScore(AnchorPane scene) {
    for (int i = 0; i < 6; i++) {
      digit[i].display(scene);
    }
  }

  public void clear(AnchorPane scene) {
    for (int i = 0; i < 6; i++) {
      digit[i].undisplay(scene);
    }
  }

}
