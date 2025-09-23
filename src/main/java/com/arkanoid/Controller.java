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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML
    private AnchorPane scene;

    private Ball ball;

    private Paddle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    public static double deltaX = 2;
    public static double deltaY = 2;

    public static final Set<KeyCode> pressedKeys = new HashSet<>();

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
    long LastTime = System.nanoTime();
        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(scene);

            paddle.checkCollisionPaddle(ball.getCircle());

            ball.getShape().setLayoutX(ball.getShape().getLayoutX() + deltaX);
            ball.getShape().setLayoutY(ball.getShape().getLayoutY() + deltaY);

            long CurrentTime = System.nanoTime();
            double DeltaTime = (CurrentTime - LastTime) / 1_000_000_000.0;
            LastTime = CurrentTime;
            System.out.println(DeltaTime);
            EnemiesManager.updateEnemies(DeltaTime);

            if (BrickManager.brick_remain > 0) {
                BrickManager.update(ball, scene);
            } else {
                gameOver();
            }

            PowerUpManager.movePowerUps(scene);
            PowerUpManager.checkCollisionPowerUps(paddle, scene);
            PowerUpManager.update(paddle, scene);

            checkCollisionScene(scene);
            checkCollisionBottomZone();
        }
    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);

        ball = new Ball(10, 10, 5);
        ball.getCircle().setLayoutX(10);
        ball.getCircle().setLayoutY(10);
        scene.getChildren().add(ball.getShape());

        BrickManager.createBricks(scene);
        EnemiesManager.CreateEnemies(scene);

        paddle = new Paddle(244, 360, 80, 10);
        scene.getChildren().add(paddle.getShape());

        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        scene.requestFocus();

        timeline.play();
    }

    public void checkCollisionScene(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = ball.getCircle().getLayoutX() >= (bounds.getMaxX() - 3 * ball.getCircle().getRadius());
        boolean leftBorder = ball.getCircle().getLayoutX() <= (bounds.getMinX() + ball.getCircle().getRadius());
        boolean bottomBorder = ball.getCircle().getLayoutY() >= (bounds.getMaxY() - ball.getCircle().getRadius());
        boolean topBorder = ball.getCircle().getLayoutY() <= (bounds.getMinY() + ball.getCircle().getRadius());

//        System.out.println(ball.getCircle().getLayoutX() + " radius: " + ball.getCircle().getRadius() + " rightBorder: " + bounds.getMaxX());

        if (rightBorder || leftBorder) {
            deltaX *= -1;
            if (PowerUpManager.powerUpState[0] > 0) {
                PowerUpManager.powerUpState[0]--;
            }
        }

        if (bottomBorder || topBorder) {
            deltaY *= -1;
            if (PowerUpManager.powerUpState[0] > 0) {
                PowerUpManager.powerUpState[0]--;
            }
        }

    }

    public void checkCollisionBottomZone() {
        if (ball.getCircle().getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
            gameOver();
        }
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
        scene.getChildren().remove(ball.getShape());
        startButton.setVisible(true);
    }

}