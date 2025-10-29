package com.arkanoid.enemies.bosses;

import com.arkanoid.Controller;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.enemies.Enemies;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.sound.Sound;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class GiantCentipedeBoss extends Enemies {

  public static ArrayList<GiantCentipedeSegment> GiantCentipede = new ArrayList<>();
  int hp = 20;
  int hitCooldown = 0;
  int dropCooldown = 80;
  int dropOrder = 0;
  int stateCooldown = 200;
  int state = 0;
  boolean isHit = false;

  public GiantCentipedeBoss(double x, double y, double r, AnchorPane scene) {
      super(x, y, r);
      double speed = 0.058;

      GiantCentipedeSegment head = new GiantCentipedeSegment(x, y, r, true, speed);
      GiantCentipede.add(head);

      for (int i = 0; i < 7; i++) {
          x -= 2 * r;
          speed -= 0.002;
          GiantCentipedeSegment seg = new GiantCentipedeSegment(x, y, r, false, speed);
          GiantCentipede.add(seg);
      }

      for (int i = 7; i >= 0; i--) {
          GiantCentipedeSegment seg = GiantCentipede.get(i);
          scene.getChildren().add(seg.getImageView());
      }

      Rectangle rect = Controller.field.getRectangle();
      state = 3;
      stateCooldown = 200;
      for (GiantCentipedeSegment seg : GiantCentipede) {
          seg.getCircle().setCenterX(rect.getX()
                  + seg.getCircle().getRadius());
          seg.getCircle().setCenterY(rect.getY()
                  - seg.getCircle().getRadius());
      }
  }

  @Override
  public void clear(AnchorPane scene) {
    for (GiantCentipedeSegment seg : GiantCentipede) {
      scene.getChildren().remove(seg.getImageView());
    }
    BrickManager.brickRemain--;
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {
    double fx = 0;
    double fy = 0;
    isHit = false;

    for (GiantCentipedeSegment seg : GiantCentipede) {
      seg.move(fx, fy, deltaTime, state);
      fx = seg.getCircle().getCenterX();
      fy = seg.getCircle().getCenterY();
      if (hitCooldown <= 0) {
        seg.changeState(0, 0);
        checkCollision(seg.getCircle());
      } else {
        seg.changeState(1, 0);
      }
    }

    if (isHit) {
      Sound.playBossHit();
      hp--;
      hitCooldown = 30;

      if (hp == 0) {
        clear(scene);
        return true;
      }
    }

    if (state == 0 && hitCooldown <= 0) {
      GiantCentipede.get(dropOrder).changeState(0,
          2 - (dropCooldown / 33));
    }

    hitCooldown--;
    dropCooldown--;
    stateCooldown--;

    switch (state) {
      case 0:

        if (dropCooldown == 0) {
          Sound.playSerpentDrop();
          dropCooldown = 80;
          double xpos = GiantCentipede.get(dropOrder).getCircle().getCenterX();
          double ypos = GiantCentipede.get(dropOrder).getCircle().getCenterY();
          Debris ndeb = new Debris(xpos, ypos, 2);
          scene.getChildren().add(ndeb.getImageView());
          EnemiesManager.addEnemy(ndeb);
          dropOrder++;
          dropOrder %= GiantCentipede.size();
        }

        if (stateCooldown <= 0
            && GiantCentipede.get(0).getMid()) {
          state = 1;
          stateCooldown = 200;
        }
        break;
      case 1:
        stateCooldown--;
        if (stateCooldown <= 0) {
          state = 2;
        }
        break;
      case 2:

        for (GiantCentipedeSegment seg : GiantCentipede) {
          if (seg.getCircle().getBoundsInParent().intersects(
              Controller.paddle.getRectangle().getBoundsInParent())) {
            EnemiesManager.setGameOver(true);
            break;
          }
        }

        Rectangle rect = Controller.field.getRectangle();
        GiantCentipedeSegment last = GiantCentipede.get(GiantCentipede.size() - 1);

        if (last.getCircle().getCenterY() - last.getCircle().getRadius()
            > rect.getY() + rect.getHeight()) {
          state = 3;
          stateCooldown = 200;
          for (GiantCentipedeSegment seg : GiantCentipede) {
            seg.getCircle().setCenterX(rect.getX()
                + seg.getCircle().getRadius());
            seg.getCircle().setCenterY(rect.getY()
                - seg.getCircle().getRadius());

          }
        }
        break;
      case 3:
        stateCooldown--;
        if (stateCooldown <= 0) {
          state = 4;
          GiantCentipede.get(0).setChanged(false);
        }
        break;
      case 4:
        if (GiantCentipede.get(0).getMid()) {
          GiantCentipede.get(0).setChanged(false);
          state = 0;
          stateCooldown = 200;
          dropCooldown = 80;
        }
        break;
      case 5:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + state);
    }

    return false;
  }

    void checkCollision(Circle circle) {
        for (Ball ball : BallManager.getBalls()) {
            if (circle.getBoundsInParent().intersects(ball.getShape().getBoundsInParent())) {
                boolean rightBorder =
                        ball.getCircle().getCenterX() >= circle.getCenterX();
                boolean leftBorder =
                        ball.getCircle().getCenterX() <= circle.getCenterX();
                boolean bottomBorder =
                        ball.getCircle().getCenterY() >= circle.getCenterY();
                boolean topBorder =
                        ball.getCircle().getCenterY() <= circle.getCenterY();

                if (rightBorder || leftBorder) {
                    ball.updateX((leftBorder ? -1 : 1));
                }

                if (bottomBorder || topBorder) {
                    ball.updateY((topBorder ? -1 : 1));
                }
                isHit = true;
            }
        }
    }
}
