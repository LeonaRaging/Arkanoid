// java
package com.arkanoid.core;

import com.arkanoid.Controller;
import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaddleTest {

  @BeforeAll
  static void initJavaFx() {

    new JFXPanel();
  }

  @BeforeEach
  void resetKeys() {
    // Ensure no keys are pressed before each test
    Controller.pressedKeys.clear();
  }

  @Test
  void movesRightWithinBounds() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    Controller.pressedKeys.add(KeyCode.RIGHT);
    paddle.update(bounds);

    assertEquals(12.0, paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void movesLeftWithinBounds() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    Controller.pressedKeys.add(KeyCode.LEFT);
    paddle.update(bounds);

    assertEquals(8.0, paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void clampsAtLeftEdge() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    paddle.getRectangle().setX(0);
    Controller.pressedKeys.add(KeyCode.LEFT);
    paddle.update(bounds);

    assertEquals(0.0, paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void clampsAtRightEdge() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    // Position so moving right would exceed bounds
    paddle.getRectangle().setX(bounds.getX() + bounds.getWidth() - paddle.getRectangle().getWidth() - 1);
    Controller.pressedKeys.add(KeyCode.RIGHT);
    paddle.update(bounds);

    assertEquals(bounds.getX() + bounds.getWidth() - paddle.getRectangle().getWidth(),
                 paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void noMovementWhenNoKeys() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    paddle.update(bounds);

    assertEquals(10.0, paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void opposingKeysCancelOut() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    Controller.pressedKeys.add(KeyCode.RIGHT);
    Controller.pressedKeys.add(KeyCode.LEFT);
    paddle.update(bounds);

    assertEquals(10.0, paddle.getRectangle().getX(), 0.0001);
  }

  @Test
  void setStateDoesNotBreakUpdate() {
    Paddle paddle = new Paddle(10, 10, 30, 10);
    Rectangle bounds = new Rectangle(0, 0, 100, 100);

    paddle.setState(1);
    // Should not throw and should still respect bounds
    Controller.pressedKeys.add(KeyCode.LEFT);
    paddle.getRectangle().setX(0);
    paddle.update(bounds);

    assertEquals(0.0, paddle.getRectangle().getX(), 0.0001);
  }
}
