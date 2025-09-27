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

    private Field field;

    @FXML
    private Button startButton;

    public static final Set<KeyCode> pressedKeys = new HashSet<>();

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(field.getRectangle());

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

            BallManager.checkCollisionScene(field.getRectangle());

            if (BallManager.checkCollisionBottomZone(scene)) {
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

        field = new Field(16, 16, 160, 208);
        scene.getChildren().add(field.getImageView());

        paddle = new Paddle(112, 210, 32, 8);
        scene.getChildren().add(paddle.getImageView());

        Ball ball = new Ball(0, 0, 2.5);
        ball.getCircle().setLayoutX(112);
        ball.getCircle().setLayoutY(200);
        ball.deltaX = 1;
        ball.deltaY = -1;
        scene.getChildren().add(ball.getImageView());
        BallManager.balls.add(ball);

        BrickManager.createBricks(scene);


        bottomZone = new Rectangle(0, 220, 256, 10);

        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        scene.requestFocus();

        timeline.play();
    }

    public void gameOver() {
        timeline.stop();
        for (Brick brick : BrickManager.bricks) {
            scene.getChildren().remove(brick.getImageView());
        }
        for (PowerUp powerUp : PowerUpManager.powerUps ){
            scene.getChildren().remove(powerUp.getImageView());
        }
        for (Entity projectile : PowerUpManager.projectiles ){
            scene.getChildren().remove(projectile.getShape());
        }
        scene.getChildren().remove(paddle.getImageView());
        scene.getChildren().remove(field.getImageView());

        BrickManager.bricks.clear();
        PowerUpManager.powerUps.clear();
        PowerUpManager.projectiles.clear();
        for (Ball ball : BallManager.balls) {
            scene.getChildren().remove(ball.getImageView());
        }
        BallManager.balls.clear();
        startButton.setVisible(true);
    }
}