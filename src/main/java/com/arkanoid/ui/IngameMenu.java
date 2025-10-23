package com.arkanoid.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IngameMenu extends MainMenu {

  @Override
  public void setChoices() {
    currentSelection = 0;
    setSpacing(8);

    HBox resume = createLabel("RESUME");
    HBox save = createLabel("SAVE");
    HBox exit = createLabel("EXIT");

    selector = new HBox();
    Image image = new Image(
        getClass().getResource("/com/arkanoid/ui/selector.png").toExternalForm());
    ImageView imageView = new ImageView(image);
    selector.getChildren().add(imageView);

    menuItems = new HBox[] {resume, save, exit};

    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }
}
