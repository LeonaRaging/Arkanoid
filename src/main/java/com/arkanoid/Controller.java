package com.arkanoid;

import com.arkanoid.Brick.Brick;
import com.arkanoid.Brick.BrickManager;
import com.arkanoid.Core.Ball;
import com.arkanoid.Core.BallManager;
import com.arkanoid.Core.Entity;
import com.arkanoid.Core.Field;
import com.arkanoid.Core.Paddle;
import com.arkanoid.Enemies.EnemiesManager;
import com.arkanoid.PowerUp.PowerUp;
import com.arkanoid.PowerUp.PowerUpManager;
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
import com.arkanoid.Score.ScoreDisplay;
import com.arkanoid.Score.HP;

public class Controller implements Initializable {
    @FXML
    private AnchorPane scene;

    private Paddle paddle;
    public static Rectangle bottomZone;
    public static Field field;
    private static Field outline;
    private static Field [] gates = new Field[4];

    private ScoreDisplay score;
    private HP hp;

    @FXML
    private Button startButton;

    public static final Set<KeyCode> pressedKeys = new HashSet<>();

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        long LastTime = System.nanoTime();
        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(field.getRectangle());

            for (Ball ball : BallManager.getBalls()) {
                paddle.checkCollisionPaddle(ball);
                ball.update(scene);
            }

            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastTime) / 1_000_000_000.0;
            LastTime = CurrentTime;
            EnemiesManager.updateEnemies(scene, DeltaTime);

            if (BrickManager.brick_remain > 0) {
                for (Ball ball : BallManager.getBalls()) {
                    BrickManager.update(ball, scene);
                }
            } else {
                gameOver();
            }

            PowerUpManager.movePowerUps(scene);
            PowerUpManager.checkCollisionPowerUps(paddle, scene);
            PowerUpManager.update(paddle, scene);
            BallManager.checkCollisionScene(field.getRectangle());

            BallManager.getBalls().removeIf(ball1->{
                if (ball1.ballType > 0) {
                    for(Ball ball: BallManager.getBalls())
                        if (ball.ballType == 0 && ball1.getCircle().getBoundsInParent().intersects(ball.getCircle().getBoundsInParent()))
                        {
                            scene.getChildren().remove(ball1.getImageView());
                            return true;
                        }
                }
                return false;
            });

            if (BallManager.checkCollisionBottomZone(scene)) {
                HP.loseLife();
                hp.updateDisplay();

                if (HP.getHp() <= 0) {
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

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);

        ScoreDisplay.setScore(0);
        HP.resetHp();

        field = new Field(16, 16, 160, 208, "field");
        outline = new Field(8, 8, 176, 216, "outline");
        gates[0] = new Field(32, 8, 32, 8, "gate0");
        gates[1] = new Field(128, 8, 32, 8, "gate0");
        gates[2] = new Field(8, 181, 8, 30, "gate1");
        gates[3] = new Field(176, 181, 8, 30, "gate1");

        scene.getChildren().add(field.getImageView());
        scene.getChildren().add(outline.getImageView());

        for (int i = 0; i < 4; i++) {
            scene.getChildren().add(gates[i].getImageView());
        }

        paddle = new Paddle(112, 210, 32, 8);
        scene.getChildren().add(paddle.getImageView());

        newLife();

        BrickManager.createBricks(scene);
        EnemiesManager.CreateEnemies(scene);

        score = new ScoreDisplay(0);
        score.showScore(scene);
        hp = new HP(scene);

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

        for (PowerUp powerUp : PowerUpManager.powerUps) {
            scene.getChildren().remove(powerUp.getImageView());
        }
        for (Entity projectile : PowerUpManager.projectiles) {
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
        for (PowerUp powerUp : PowerUpManager.powerUps ){
            scene.getChildren().remove(powerUp.getImageView());
        }
        for (Entity projectile : PowerUpManager.projectiles ){
            scene.getChildren().remove(projectile.getImageView());
        }
        scene.getChildren().remove(paddle.getImageView());
        scene.getChildren().remove(field.getImageView());
        scene.getChildren().remove(outline.getImageView());
        for (int i = 0; i < 4; i++) {
            scene.getChildren().remove(gates[i].getImageView());
        }

        /*
        clear hp and score
         */
        score.clear(scene);
        hp.clear(scene);

        BrickManager.getBricks().clear();
        PowerUpManager.powerUps.clear();
        PowerUpManager.projectiles.clear();
        for (Ball ball : BallManager.getBalls()) {
            scene.getChildren().remove(ball.getImageView());
        }
        BallManager.getBalls().clear();

        startButton.setVisible(true);
    }
}
