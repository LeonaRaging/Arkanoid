package com.arkanoid.core;

import com.arkanoid.Controller;
import com.arkanoid.sound.Sound;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class BallManager {
  private BallManager() {
    throw new IllegalStateException("Utility class");
  }

  private static ArrayList<Ball> balls = new ArrayList<>();
  private static int isCaught = 0;

  public static int getIsCaught() {
    return isCaught;
  }

  public static void setIsCaught(int isCaught) {
    BallManager.isCaught = isCaught;
  }

  public static ArrayList<Ball> getBalls() {
    return balls;
  }

  public static void checkCollisionScene(Rectangle rect) {
    for (Ball ball : balls) {
      boolean rightBorder = ball.getCircle().getLayoutX()
          >= (rect.getX() + rect.getWidth() - ball.getCircle().getRadius());
      boolean leftBorder = ball.getCircle().getLayoutX()
          <= (rect.getX() + ball.getCircle().getRadius());
      boolean bottomBorder = ball.getCircle().getLayoutY()
          >= (rect.getY() + rect.getHeight() - ball.getCircle().getRadius());
      boolean topBorder = ball.getCircle().getLayoutY()
          <= (rect.getY() + ball.getCircle().getRadius());

      if (rightBorder || leftBorder) {
        ball.updateX((rightBorder ? -1 : 1));
        Sound.playBounceField();
      }

      if (bottomBorder || topBorder) {
        ball.updateY(1);
        Sound.playBounceField();
      }

    }
  }

  public static boolean checkCollisionBottomZone(AnchorPane scene) {
    balls.removeIf(ball -> {
      if (ball.getCircle().getBoundsInParent()
          .intersects(Controller.bottomZone.getBoundsInParent())) {
        scene.getChildren().remove(ball.getImageView());
        for (int i = 0; i < 6; i++) {
          scene.getChildren().remove(ball.getImageViews()[i]);
        }
        return true;
      }
      return false;
    });
    return (balls.isEmpty() && isCaught == 0);
  }

}
