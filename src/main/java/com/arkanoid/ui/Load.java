package com.arkanoid.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Load extends MainMenu {
  @Override
  public void setChoices() {
    currentSelection = 0;
    setSpacing(8);

    HBox load1 = createLabel("1. LOAD SLOT 1");
    HBox load2 = createLabel("2. LOAD SLOT 2");
    HBox load3 = createLabel("3. LOAD SLOT 3");

    selector = new HBox();
    Image image = new Image(
        getClass().getResource("/com/arkanoid/ui/selector.png").toExternalForm());
    ImageView imageView = new ImageView(image);
    selector.getChildren().add(imageView);

    menuItems = new HBox[] {load1, load2, load3};

    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }
}
