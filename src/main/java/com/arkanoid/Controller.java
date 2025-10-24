package com.arkanoid;

import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import com.arkanoid.core.Paddle;
import com.arkanoid.enemies.Enemies;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.field.Field;
import com.arkanoid.field.Gate;
import com.arkanoid.powerup.PowerUp;
import com.arkanoid.powerup.PowerUpManager;
import com.arkanoid.score.Hp;
import com.arkanoid.score.ScoreDisplay;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

public class Controller implements Initializable {

  @FXML
  private AnchorPane scene;

  private Paddle paddle;
  public static Rectangle bottomZone;
  public static Field field;
  private static Field outline;
  public static Gate[] gates = new Gate[4];

  private ScoreDisplay score;
  private Hp hp;

  private int level;

  @FXML private Button startButton;
  @FXML private ImageView startBackground;
  @FXML private ImageView backgroundView;
  @FXML private ImageView backgroundView11;
  @FXML private ImageView backgroundViewother;

  public static final Set<KeyCode> pressedKeys = new HashSet<>();

  Timeline timeline = new Timeline(
          new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(ActionEvent actionEvent) {
              paddle.update(field.getRectangle());

              for (Ball ball : BallManager.getBalls()) {
                paddle.checkCollisionPaddle(ball);
                ball.update(scene);
              }

              long currentTime = System.nanoTime();
              double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
              lastTime = currentTime;

              EnemiesManager.update(scene);
              EnemiesManager.updateEnemies(scene, deltaTime);

              brickUpdate();

              powerUpUpdate();

              ballUpdate();

              gateUpdate();

              if (BallManager.checkCollisionBottomZone(scene)) {
                Hp.loseLife();
                hp.updateDisplay();

                if (Hp.getHp() <= 0) {
                  gameOver();
                } else {
                  newLife();
                }
              }

              if (score != null) {
                score.reup();
              }
            }
          }));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  void moveNext(boolean clear) {
    if (clear == true) {
      scene.getChildren().remove(field.getImageView());
      scene.getChildren().remove(outline.getImageView());

      for (int i = 0; i < 4; i++) {
        scene.getChildren().remove(gates[i].getImageView());
      }

      scene.getChildren().remove(paddle.getImageView());
    }
    field = new Field(16, 16, 160, 208, Integer.toString(level));
    outline = new Field(8, 8, 176, 216, "outline");
    gates[0] = new Gate(32, 8, 32, 8, "gate0");
    gates[1] = new Gate(128, 8, 32, 8, "gate0");
    gates[2] = new Gate(8, 47, 8, 30, "gate1");
    gates[3] = new Gate(176, 47, 8, 30, "gate1");

    scene.getChildren().add(field.getImageView());
    scene.getChildren().add(outline.getImageView());

    for (int i = 0; i < 4; i++) {
      scene.getChildren().add(gates[i].getImageView());
    }

    paddle = new Paddle(112, 210, 32, 8);
    scene.getChildren().add(paddle.getImageView());
  }
  @FXML
  void startGameButtonAction(ActionEvent event) {

    startButton.setVisible(false);
    startBackground.setVisible(false);
    backgroundView.setVisible(true);

    ScoreDisplay.setScore(0);
    Hp.resetHp();
    level = 1;
    moveNext(false);

    newLife();

    BrickManager.createBricks(scene, level);

    score = new ScoreDisplay(0);
    score.showScore(scene);
    hp = new Hp(scene);

    bottomZone = new Rectangle(0, 220, 256, 10);

    scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
    scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
    scene.requestFocus();

    timeline.play();
  }

  private void newLife() {
    for (Ball ball : BallManager.getBalls()) {
      scene.getChildren().remove(ball.getImageView());
    }
    BallManager.getBalls().clear();

    for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
      scene.getChildren().remove(powerUp.getImageView());
    }
    for (Entity projectile : PowerUpManager.getProjectiles()) {
      scene.getChildren().remove(projectile.getImageView());
    }
    PowerUpManager.resetPower();

    paddle.getRectangle().setX(112);
    paddle.getRectangle().setY(210);
    paddle.setState(0);

    Ball ball = new Ball(0, 0, 2.5);
    ball.getCircle().setLayoutX(112);
    ball.getCircle().setLayoutY(200);
    ball.setDeltaX(1);
    ball.setDeltaY(1);
    scene.getChildren().add(ball.getImageView());
    BallManager.getBalls().add(ball);
  }

  public void gameOver() {
    timeline.stop();

    for (Brick brick : BrickManager.getBricks()) {
      scene.getChildren().remove(brick.getImageView());
    }
    for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
      scene.getChildren().remove(powerUp.getImageView());
    }
    for (Entity projectile : PowerUpManager.getProjectiles()) {
      scene.getChildren().remove(projectile.getImageView());
    }
    for (Enemies enemy :  EnemiesManager.getEnemies()) {
      scene.getChildren().remove(enemy.getImageView());
    }
    scene.getChildren().remove(paddle.getImageView());
    scene.getChildren().remove(field.getImageView());
    scene.getChildren().remove(outline.getImageView());

    for (int i = 0; i < 4; i++) {
      scene.getChildren().remove(gates[i].getImageView());
    }

    EnemiesManager.removeAllEnemies(scene);

    /*
    clear hp and score
     */
    score.clear(scene);
    hp.clear(scene);

    BrickManager.getBricks().clear();
    PowerUpManager.getPowerUps().clear();
    PowerUpManager.getProjectiles().clear();
    EnemiesManager.getEnemies().clear();
    for (Ball ball : BallManager.getBalls()) {
      scene.getChildren().remove(ball.getImageView());
    }
    BallManager.getBalls().clear();

    startButton.setVisible(true);
    startBackground.setVisible(true);
    backgroundView.setVisible(false);
  }

  private void brickUpdate() {
    if (BrickManager.brickRemain > 0) {
      for (Ball ball : BallManager.getBalls()) {
        BrickManager.update(ball, scene);
      }
    } else {
      level++;
      // will replace as boss level later
      if (level == 5 || level == 10 || level == 15) {
        level++;
      }
      if (level > 15) {
        gameOver();
      }
      if (level==11){
        backgroundView.setVisible(false);
        backgroundView11.setVisible(true);
      }
      if (level==12){
        backgroundView11.setVisible(false);
        backgroundViewother.setVisible(true);
      }
      moveNext(true);
      newLife();
      for (Brick brick : BrickManager.getBricks()) {
        scene.getChildren().remove(brick.getImageView());
      }
      BrickManager.getBricks().clear();
      BrickManager.createBricks(scene, level);
    }
  }

  private void powerUpUpdate() {
    PowerUpManager.movePowerUps(scene);
    PowerUpManager.checkCollisionPowerUps(paddle, scene);
    PowerUpManager.update(paddle, scene);
    BallManager.checkCollisionScene(field.getRectangle());
  }

  private void ballUpdate() {
    BallManager.getBalls().removeIf(ball1 -> {
      if (ball1.ballType > 0) {
        for (Ball ball : BallManager.getBalls()) {
          if (ball.ballType == 0 && ball1.getCircle().getBoundsInParent()
                  .intersects(ball.getCircle().getBoundsInParent())) {
            scene.getChildren().remove(ball1.getImageView());
            return true;
          }
        }
      }
      return false;
    });
  }

  private void gateUpdate() {
    for (int i = 0; i < 4; i++) {
      gates[i].update(i);
    }
  }
}
