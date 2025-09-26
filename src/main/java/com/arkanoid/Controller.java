package com.arkanoid;

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

public class Controller implements Initializable {
    @FXML
    private AnchorPane scene;

    private Paddle paddle;

    public static Rectangle bottomZone;

    @FXML
    private Button startButton;

    public static final Set<KeyCode> pressedKeys = new HashSet<>();

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(scene);

            for (Ball ball : BallManager.balls) {
                paddle.checkCollisionPaddle(ball);
                ball.update(scene);
            }

            if (BrickManager.brick_remain > 0) {
                for (Ball ball : BallManager.balls) {
                    BrickManager.update(ball, scene);
                }
            } else {
                gameOver();
            }

            PowerUpManager.movePowerUps(scene);
            PowerUpManager.checkCollisionPowerUps(paddle, scene);
            PowerUpManager.update(paddle, scene);

            BallManager.checkCollisionScene(scene);

            System.out.println(BallManager.balls.size());

            if (BallManager.checkCollisionBottomZone()) {
                gameOver();
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

        Ball ball = new Ball(5, 5, 2.5);
        ball.getCircle().setLayoutX(10);
        ball.getCircle().setLayoutY(10);
        ball.deltaX = ball.deltaY = 1;
        scene.getChildren().add(ball.getShape());
        BallManager.balls.add(ball);

        BrickManager.createBricks(scene);

        paddle = new Paddle(112, 210, 32, 8);
        scene.getChildren().add(paddle.getShape());

        bottomZone = new Rectangle(0, 230, 256, 10);

        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        scene.requestFocus();

        timeline.play();
    }

    public void gameOver() {
        timeline.stop();
        for (Brick brick : BrickManager.bricks) {
            scene.getChildren().remove(brick.getShape());
        }
        for (PowerUp powerUp : PowerUpManager.powerUps ){
            scene.getChildren().remove(powerUp.getShape());
        }
        for (Entity projectile : PowerUpManager.projectiles ){
            scene.getChildren().remove(projectile.getShape());
        }
        BrickManager.bricks.clear();
        PowerUpManager.powerUps.clear();
        PowerUpManager.projectiles.clear();
        scene.getChildren().remove(paddle.getShape());
        for (Ball ball : BallManager.balls) {
            scene.getChildren().remove(ball.getShape());
        }
        BallManager.balls.clear();
        startButton.setVisible(true);
    }
}