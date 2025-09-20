package com.arkanoid;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double deltaX = 2;
    double deltaY = 2;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent actionEvent) {
            movepaddle();

            checkCollisionPaddle(paddle);

            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if (!bricks.isEmpty()) {
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else {
                startButton.setVisible(true);
                timeline.stop();
            }

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
        startGame();
    }

    public void startGame() {
        circle.setLayoutX(10);
        circle.setLayoutY(10);
        createBricks();
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

    public boolean checkCollisionBrick(Rectangle brick){

        if(circle.getBoundsInParent().intersects(brick.getBoundsInParent())){
            boolean rightBorder = circle.getLayoutX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (brick.getX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (brick.getY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }

            scene.getChildren().remove(brick);

            return true;
        }
        return false;
    }

    public void createBricks() {
        double width = 560;
        double height = 200;

        int spaceCheck = 1;

        for (double i = height; i > 0; i -= 50) {
            for (double j = width; j > 0; j -= 25) {
                if (spaceCheck % 2 == 0) {
                    Rectangle rectangle = new Rectangle(j, i, 30, 30);
                    rectangle.setFill(Color.RED);
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    public void checkCollisionBottomZone() {
        if (circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
            timeline.stop();
            for (Rectangle brick : bricks) {
                scene.getChildren().remove(brick);
            }
            bricks.clear();
            startButton.setVisible(true);
        }
    }

    public void movepaddle() {
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = paddle.getWidth();

        if (xPos >= sceneXPos + (paddleWidth / 2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth / 2));
        } else if (xPos < sceneXPos + (paddleWidth / 2)) {
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    public void checkCollisionPaddle(Rectangle paddle) {
        if (circle.getBoundsInParent().intersects(paddle.getBoundsInParent())) {

            boolean rightBorder = circle.getLayoutX() >= ((paddle.getLayoutX() + paddle.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutX() >= ((paddle.getLayoutY() + paddle.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (paddle.getLayoutY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }
        }
    }
}