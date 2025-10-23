package com.arkanoid.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Save extends MainMenu {
  @Override
  public void setChoices() {
    currentSelection = 0;
    setSpacing(8);

    HBox save1 = createLabel("1. SAVE SLOT 1");
    HBox save2 = createLabel("2. SAVE SLOT 2");
    HBox save3 = createLabel("3. SAVE SLOT 3");

    selector = new HBox();
    Image image = new Image(
        getClass().getResource("/com/arkanoid/ui/selector.png").toExternalForm());
    ImageView imageView = new ImageView(image);
    selector.getChildren().add(imageView);

    menuItems = new HBox[] {save1, save2, save3};

    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }
}
