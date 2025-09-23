package com.arkanoid;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Paddle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    public static double deltaX = 2;
    public static double deltaY = 2;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(scene);

            paddle.checkCollisionPaddle(circle);

            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if (BrickManager.brick_remain > 0) {
                BrickManager.update(circle, scene);
            } else {
                startButton.setVisible(true);
                timeline.stop();
            }

            PowerUpManager.movePowerUps(scene);
            PowerUpManager.checkCollisionPowerUps(paddle, scene);

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

        circle.setLayoutX(10);
        circle.setLayoutY(10);

        BrickManager.createBricks(scene);

        paddle = new Paddle(244, 360, 80, 10);
        scene.getChildren().add(paddle.getPos());

        timeline.play();
    }

    public void checkCollisionScene(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
        boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());

        if (rightBorder || leftBorder) {
            deltaX *= -1;
        }

        if (bottomBorder || topBorder) {
            deltaY *= -1;
        }

    }

    public void checkCollisionBottomZone() {
        if (circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
            timeline.stop();
            for (Brick brick : BrickManager.bricks) {
                scene.getChildren().remove(brick.getPos());
            }
            BrickManager.bricks.clear();
            scene.getChildren().remove(paddle.getPos());
            startButton.setVisible(true);
        }
    }

}