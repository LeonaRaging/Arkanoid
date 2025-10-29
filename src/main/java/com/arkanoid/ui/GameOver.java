package com.arkanoid.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GameOver extends MainMenu {
  @Override
  public void setChoices() {
    setSpacing(8);

    HBox yes = createLabel("YES");
    HBox no = createLabel("NO");

    selector = new HBox();
    Image image = new Image(
        getClass().getResource("/com/arkanoid/ui/selector.png").toExternalForm());
    ImageView imageView = new ImageView(image);
    selector.getChildren().add(imageView);

    menuItems = new HBox[] {yes, no};

    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }
}
