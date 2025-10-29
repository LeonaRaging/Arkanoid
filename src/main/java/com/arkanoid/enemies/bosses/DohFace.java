package com.arkanoid.enemies.bosses;

import com.arkanoid.Controller;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.enemies.Enemies;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.sound.Sound;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class DohFace extends Enemies {
  /*
   * Note:
   * The only way to avoid the attack of form 1
   * is to move close to the right or left border
   * right after the first laser passes.
   * The ball should move more slowly in this round.
   * Very hard challenge.
   */

  private int hp = 40;
  private int state;
  private int stateCooldown;
  private MiniDohFace[] child = new MiniDohFace[4];
  private final Image[][] images = new Image[5][40];
  private int imageState;
  private int imageCooldown;
  private int imageDisplay;
  private boolean isHit = false;
  private int imageHit;
  private int hitCooldown;
  private Rectangle[] eyes = new Rectangle[2];
  public static ArrayList<Firework> fireworks = new ArrayList<>();
  private int fireworkCooldown = 30;

  public DohFace(AnchorPane scene) {
    super(0, 0, 44, 91);
    Rectangle rect = Controller.field.getRectangle();
    double xPos = rect.getX() + rect.getWidth() / 2;
    double yPos = rect.getY() + rect.getHeight() / 2;
    this.getRectangle().setX(xPos - this.getRectangle().getWidth() / 2);
    this.getRectangle().setY(yPos - this.getRectangle().getHeight() / 2 - 30);

    // hitbox = rectangle 44x91, image -> bottom + 1 pixel

    // each image is Ax95
    for (int i = 0; i < 22; i++) {
      images[0][i] = new Image(getClass().getResource(
          "/com/arkanoid/Enemy/DohFace/DohFace0" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 8; i++) {
      images[1][i] = new Image(getClass().getResource(
          "/com/arkanoid/Enemy/DohFace/DohFace1" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 6; i++) {
      images[2][i] = new Image(getClass().getResource(
          "/com/arkanoid/Enemy/DohFace/DohFace2" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 35; i++) {
      images[3][i] = new Image(getClass().getResource(
          "/com/arkanoid/Enemy/DohFace/DohFace3" + i + ".png").toExternalForm());
    }
    for (int i = 0; i < 40; i++) {
      images[4][i] = new Image(getClass().getResource(
          "/com/arkanoid/Enemy/DohFace/DohFace4" + i + ".png").toExternalForm());
    }

    for (int i = 0; i < 4; i++) {
      child[i] = null;
    }

    state = 0;
    stateCooldown = 200;
    imageDisplay = 0;
    imageState = 0;
    imageCooldown = 5;
    hideBalls(scene);
  }

  public void hideBalls(AnchorPane scene) {
    for (Ball ball : BallManager.getBalls()) {
      ball.setDeltaX(0);
      ball.setDeltaY(0);
    }
  }

  public void showBalls(AnchorPane scene) {
    for (Ball ball : BallManager.getBalls()) {
      ball.setDeltaX(0.3);
      ball.setDeltaY(-0.3);
    }
  }

  @Override
  public void clear(AnchorPane scene) {
    for (int i = 0; i < 4; ++i) {
      if (child[i] != null) {
        child[i].clear(scene);
        child[i] = null;
      }
    }
    scene.getChildren().remove(this.getImageView());
  }

  @Override
  public boolean update(double deltaTime, AnchorPane scene) {

    // Update for Mini-faces
    for (int i = 0; i < 4; i++) {
      if (child[i] != null) {
        if (child[i].move(state)) {
          scene.getChildren().remove(child[i].getImageView());
          child[i] = null;
        }
      }
    }

    if (state < 5) { // Boss in first form
      if (!isHit) {
        imageView.setImage(images[imageDisplay][imageState]);
        imageView.setX(this.getRectangle().getX()
            + this.getRectangle().getWidth() / 2
            - 80);
        imageView.setY(this.getRectangle().getY()
            + this.getRectangle().getHeight() / 2
            - imageView.getImage().getHeight() / 2);
        checkCollision();

        if (state == 4) {
          isHit = false;
        }

        if (isHit) {
          Sound.playBossHit();
          hp--;
          if (hp <= 0) {
            state = 5;
            imageState = 0;
            imageCooldown = 5;
            imageDisplay = 3;

            for (int i = 0; i < 4; i++) {
              if (child[i] != null) {
                child[i].setDeath();
              }
            }
          } else {
            imageDisplay = 1;
            imageHit = 0;
            hitCooldown = 5;
          }
        }
      } else { // Being hit in first form
        imageView.setImage(images[imageDisplay][imageHit]);
        imageView.setX(this.getRectangle().getX()
            + this.getRectangle().getWidth() / 2
            - 80);
        imageView.setY(this.getRectangle().getY()
            + this.getRectangle().getHeight() / 2
            - imageView.getImage().getHeight() / 2);

        hitCooldown--;
        if (hitCooldown <= 0) {
          imageHit++;
          hitCooldown = 5;

          if (imageHit > 7) {
            isHit = false;
            imageDisplay = 2;
          }
        }

      }
    }

    switch (state) {
      case 0: // appear
        imageCooldown--;
        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 5;

          if (imageState > 21) {
            state = 1;
            imageState = 0;
            imageDisplay = 2;
            stateCooldown = 2;
            imageCooldown = 20;
          }
        }

        break;
      case 1: // Roar + Ball spawn
        imageCooldown--;
        if (imageCooldown <= 0) {
          imageState++;
          imageCooldown = 20;

          if (imageState > 5) {
            stateCooldown--;
            if (stateCooldown > 0) {
              imageState = 0;
            } else {
              // Ball should spawn here <-------
              showBalls(scene);
              state = 2;
              stateCooldown = 400;
              imageState = 0;
              imageCooldown = 20;
            }
          }
        }

        break;
      case 2: // Summon 4 mini-faces in 4 directions
        // Any direction with a mini-face will not summon anymore
        // Then let mini-faces move within its area
        if (isHit) {
          break;
        }

        stateCooldown--;
        imageCooldown--;

        if (stateCooldown <= 0) {
          state = 3;
          imageState = 0;
          imageCooldown = 20;
        }

        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 20;

          if (imageState == 3) {
            for (int i = 0; i < 4; i++) {
              if (child[i] == null) {
                double centerX = this.getRectangle().getX()
                    + this.getRectangle().getWidth() / 2;
                double centerY = this.getRectangle().getY()
                    + this.getRectangle().getHeight() / 2;
                child[i] = new MiniDohFace(centerX - 7, centerY - 12, i);
                scene.getChildren().add(child[i].getImageView());
              }
            }
          }

          if (imageState > 5) {
            imageState = 0;
            imageCooldown = 1000;
          }
        }

        break;
      case 3, 4: // Mini-faces into positions + Shoot laser
        boolean readyShoot = true;
        for (int i = 0; i < 4; i++) {
          if (child[i] != null) {
            if (!child[i].isReadyShoot()) {
              readyShoot = false;
            }
          }
        }

        if (readyShoot) { // When everything is in place...
          state = 4;
          imageCooldown--;
          if (imageCooldown <= 0) {
            imageState++;
            imageCooldown = 20;

            if (imageState == 3) { // ...and the boss is opening his mouth...
              BossLaser bossLaser = new BossLaser(imageView.getX() + 76,
                  imageView.getY() + 51, 8, 0, scene);
              EnemiesManager.addEnemy(bossLaser);

              for (int i = 0; i < 4; i++) { // ...shoot
                if (child[i] != null && !child[i].isDeath()) {
                  double xpos = child[i].getRectangle().getX();
                  double ypos = child[i].getRectangle().getY()
                      + child[i].getRectangle().getHeight();
                  Laser nl = new Laser(xpos + 5, ypos - 10);
                  scene.getChildren().add(nl.getImageView());
                  EnemiesManager.addEnemy(nl);
                }
              }
            }

            if (imageState > 5) {
              state = 2;
              stateCooldown = 400;
              imageState = 0;
              imageCooldown = 20;
            }
          }
        }

        break;
      case 5: // Transform into cool-face
        imageView.setImage(images[imageDisplay][imageState]);
        imageView.setX(this.getRectangle().getX()
            + this.getRectangle().getWidth() / 2 - 48);
        imageView.setY(this.getRectangle().getY()
            + this.getRectangle().getHeight() / 2
            - imageView.getImage().getHeight() / 2);

        imageCooldown--;
        if (imageCooldown == 0) {
          imageState++;
          imageCooldown = 5;

          if (imageState > 34) {
            state = 6;
            imageState = 0;
            imageDisplay = 0;
            stateCooldown = 200;
            hp = 2;
            isHit = false;
            this.getRectangle().setX(this.getRectangle().getX() - 4);
            this.getRectangle().setWidth(this.getRectangle().getWidth() + 8);
            this.getRectangle().setHeight(this.getRectangle().getHeight() + 3);
            imageView.setY(this.getRectangle().getY() - 20);
            imageView.setX(this.getRectangle().getX()
                + this.getRectangle().getWidth() / 2 - 80);
            eyes[0] = new Rectangle(imageView.getX() + 61,
                imageView.getY() + 38, 7, 1);
            eyes[1] = new Rectangle(imageView.getX() + 91,
                imageView.getY() + 38, 7, 1);
          }
        }
        break;
      case 6: // Shoot laser (from eyes)
        imageView.setImage(images[4][imageState + imageDisplay]);
        imageView.setY(this.getRectangle().getY() - 20);
        imageView.setX(this.getRectangle().getX()
            + this.getRectangle().getWidth() / 2 - 80);
        stateCooldown--;

        if (!isHit) {
          checkCollision();

          if (isHit) {
            hp--;

            if (hp <= 0) {
              imageDisplay = 0;
              hp = 2;
              imageState += 8;
              if (imageState > 39) {
                hideBalls(scene);
                state = 7;
                imageState -= 8;
                imageCooldown = 5;
              }
              this.getRectangle().setX(this.getRectangle().getX() - 5);
              this.getRectangle().setWidth(this.getRectangle().getWidth() + 10);
              this.getRectangle().setHeight(this.getRectangle().getHeight() + 3);
              eyes[0].setX(eyes[0].getX() - 1);
              eyes[0].setY(eyes[0].getY() + 1);
              eyes[1].setX(eyes[1].getX() + 1);
              eyes[1].setY(eyes[1].getY() + 1);
            } else {
              imageDisplay = 0;
              imageCooldown = 5;
            }
          }
        } else {
          imageCooldown--;
          if (imageCooldown <= 0) {
            imageDisplay++;
            if (imageDisplay > 7) {
              imageDisplay = 0;
              isHit = false;
            }
          }
        }

        if (stateCooldown <= 0) {
          stateCooldown = 200;
          for (int i = 0; i < 2; i++) {
            BossLaser bossLaser = new BossLaser(eyes[i].getX(),
                eyes[i].getY(), eyes[i].getWidth(), 1, scene);
            EnemiesManager.addEnemy(bossLaser);
          }
        }

        break;
      case 7: // Become small again
        imageView.setImage(images[4][imageState]);
        imageCooldown--;

        if (imageCooldown <= 0) {
          imageState -= 8;
          imageCooldown = 5;

          if (imageState < 0) {
            state = 8;
            imageState = 0;
            imageDisplay = 1;
            stateCooldown = 20;
          }
        }
        break;
      case 8: // Spin + dead
        imageView.setImage(images[3][imageState]);
        imageView.setX(this.getRectangle().getX()
            + this.getRectangle().getWidth() / 2 - 48);
        imageView.setY(this.getRectangle().getY()
            + this.getRectangle().getHeight() / 2
            - imageView.getImage().getHeight() / 2);
        imageState += imageDisplay;
        fireworkCooldown--;

        if (fireworks.size() < 3 && fireworkCooldown <= 0) {
          fireworkCooldown = 30;
          Firework nf = new Firework();
          while (fireworks.stream()
              .anyMatch(x -> x.getShape()
                  .getBoundsInParent()
                  .intersects(nf.getShape().getBoundsInParent()))) {
            nf.changePos();
          }
          fireworks.add(nf);
          scene.getChildren().add(nf.getImageView());
        }

        fireworks.removeIf(firework -> {
          if (firework.move()) {
            scene.getChildren().remove(firework.getImageView());
            return true;
          }
          return false;
        });

        if (imageState < 0 || imageState > 34) {
          imageDisplay *= -1;
          imageState += imageDisplay;
          stateCooldown--;

          if (stateCooldown <= 0) {
            for (Firework firework : fireworks) {
              scene.getChildren().remove(firework.getImageView());
            }
            EnemiesManager.setGameOver(true);
            return true;
          }
        }

        break;
      default:
        throw new IllegalStateException("Unexpected value: " + state);
    }

    return false;
  }

  void checkCollision() {
    for (Ball ball : BallManager.getBalls()) {
      if (this.getShape().getBoundsInParent().intersects(ball.getShape().getBoundsInParent())) {
        Rectangle rect = this.getRectangle();
        boolean rightBorder =
            ball.getCircle().getLayoutX() >= (rect.getX() + rect.getWidth() - ball.getCircle()
                .getRadius());
        boolean leftBorder =
            ball.getCircle().getLayoutX() <= (rect.getX() + ball.getCircle().getRadius());
        boolean bottomBorder =
            ball.getCircle().getLayoutY() >= (rect.getY() + rect.getHeight() - ball.getCircle()
                .getRadius());
        boolean topBorder =
            ball.getCircle().getLayoutY() <= (rect.getY() + ball.getCircle().getRadius());

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

