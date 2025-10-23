package com.arkanoid.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ScoreBoard extends VBox {
  private ArrayList<Integer> scores = new ArrayList<>();

  HBox createScoreRow(String scoreStr) {
    HBox hbox = new HBox();
    for (int i = 0; i < scoreStr.length(); i++) {
      char c = scoreStr.charAt(i);
      ImageView imageView = new ImageView(FontManager.blueFont.getCharImage(c));
      hbox.getChildren().add(imageView);
    }
    return hbox;
  }

  public void setScore() throws FileNotFoundException {
    getChildren().clear();
    setSpacing(8);

    HBox scoreLabel = createScoreRow("SCORE BOARD");
    scoreLabel.setAlignment(Pos.CENTER);
    getChildren().add(scoreLabel);

    File file = new File("src/main/resources/com/arkanoid/ui/score.txt");
    Scanner sc = new Scanner(file);
    scores.clear();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (!line.isEmpty()) {
        int score = Integer.parseInt(line);
        scores.add(score);
      }
    }

    Collections.sort(scores, Collections.reverseOrder());
    for (int i = 0; i < Math.min(9, scores.size()); i++) {
      String score = scores.get(i).toString();
      while (score.length() < 6) {
        score = "0" + score;
      }
      String scoreStr = String.format("%d" + ". " + score, i + 1);
      HBox scoreRow = createScoreRow(scoreStr);
      scoreRow.setAlignment(Pos.CENTER);
      getChildren().add(scoreRow);
    }
  }
}
