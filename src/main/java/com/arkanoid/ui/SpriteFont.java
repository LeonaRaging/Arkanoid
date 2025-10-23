package com.arkanoid.ui;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpriteFont {
  private Image spriteSheet;
  private final Map<Character, Image> charMap = new HashMap<>();
  private final int charWidth = 8;
  private final int charHeight = 8;

  public SpriteFont(String spriteName) {
    this.spriteSheet = new Image(getClass()
        .getResource("/com/arkanoid/ui/" + spriteName).toExternalForm());
    loadCharacters();
  }

  public void loadCharacters() {
    String charset = " !\"#$%&'()*+,-./0123456789:;<.>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "[¥]^_>abcdefghijklmnopqrstuvwxyz{|}~";
    int cols = (int) (spriteSheet.getWidth() / charWidth);
    for (int i = 0; i < charset.length(); i++) {
      int x = (i % cols) * charWidth;
      int y = (i / cols) * charHeight;
      WritableImage charImage = new WritableImage(spriteSheet.getPixelReader(),
          x, y, charWidth, charHeight);
      charMap.put(charset.charAt(i), charImage);
    }
  }

  public Image getCharImage(char c) {
    return charMap.getOrDefault(c, null);
  }
}
