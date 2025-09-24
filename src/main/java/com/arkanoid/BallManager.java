package com.arkanoid;

import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class BallManager {
  public static ArrayList<Ball> balls = new ArrayList<>();

  public static void checkCollisionScene(Node node) {
    Bounds bounds = node.getBoundsInLocal();
    for (Ball ball : balls) {
      boolean rightBorder = ball.getCircle().getLayoutX() >= (bounds.getMaxX() - 3 * ball.getCircle().getRadius());
      boolean leftBorder = ball.getCircle().getLayoutX() <= (bounds.getMinX() + ball.getCircle().getRadius());
      boolean bottomBorder = ball.getCircle().getLayoutY() >= (bounds.getMaxY() - ball.getCircle().getRadius());
      boolean topBorder = ball.getCircle().getLayoutY() <= (bounds.getMinY() + ball.getCircle().getRadius());

      if (rightBorder || leftBorder) {
        ball.deltaX *= -1;
        if (PowerUpManager.powerUpState[0] > 0) {
          PowerUpManager.powerUpState[0]--;
        }
      }

      if (bottomBorder || topBorder) {
        ball.deltaY *= -1;
        if (PowerUpManager.powerUpState[0] > 0) {
          PowerUpManager.powerUpState[0]--;
        }
      }

    }
  }

  public static boolean checkCollisionBottomZone() {
    balls.removeIf(ball -> ball.getCircle().getBoundsInParent().intersects(Controller.bottomZone.getBoundsInParent()));
    return balls.isEmpty();
  }

}
