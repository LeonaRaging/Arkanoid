package com.arkanoid.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainMenu extends VBox {
  protected HBox selector;
  protected static int currentSelection = 0;
  protected HBox[] menuItems;

  protected HBox createLabel(String text) {
    HBox hbox = new HBox();
    for (int i = 0; i < text.length(); i++) {
      Image image = FontManager.whiteFont.getCharImage(text.charAt(i));
      ImageView imageView = new ImageView(image);
      hbox.getChildren().add(imageView);
    }
    return hbox;
  }

  protected HBox createMenuRow(HBox item, boolean isSelected) {
    if (isSelected) {
      HBox hbox = new HBox();
      hbox.setSpacing(8);
      hbox.getChildren().add(selector);
      hbox.getChildren().add(item);
      return hbox;
    }
    return item;
  }

  public void setChoices() {
    setSpacing(8);

    HBox play = createLabel("PLAY");
    HBox load = createLabel("LOAD");
    HBox scoreBoard = createLabel("SCOREBOARD");

    selector = new HBox();
    Image image = new Image(
        getClass().getResource("/com/arkanoid/ui/selector.png").toExternalForm());
    ImageView imageView = new ImageView(image);
    selector.getChildren().add(imageView);

    menuItems = new HBox[] {play, load, scoreBoard};

    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }

  public void moveSelector(int direction) {
    currentSelection += direction;
    currentSelection = (currentSelection + menuItems.length) % menuItems.length;
  }

  public void update() {
    getChildren().clear();
    for (int i = 0; i < menuItems.length; i++) {
      getChildren().add(createMenuRow(menuItems[i], i == currentSelection));
    }
  }

  public static int getCurrentSelection() {
    return currentSelection;
  }
}
