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

    private ArrayList<Brick> bricks = new ArrayList<>();

    private ArrayList<PowerUps> powerUps = new ArrayList<>();

    private int powerUpState;

    public static double deltaX = 2;
    public static double deltaY = 2;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent actionEvent) {
            paddle.update(scene);

            paddle.checkCollisionPaddle(circle);

            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if (!bricks.isEmpty()) {
                bricks.removeIf(brick -> {
                    brick.checkCollisionBrick(circle);
                   if (brick.getHP() == 0) {
                       scene.getChildren().remove(brick.getPos());
                       createPowerUps(brick);
                       return true;
                   }
                   return false;
                });
            } else {
                startButton.setVisible(true);
                timeline.stop();
            }

            movePowerUps();

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

        createBricks();

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

    public void createBricks() {
        int width = 560;
        int height = 200;

        int spaceCheck = 1;

        Color[] ColorSamples = new Color[9];
        ColorSamples[0] = Color.WHITE;
        ColorSamples[1] = Color.GREEN;
        ColorSamples[2] = Color.ORANGE;
        ColorSamples[3] = Color.BLUE;
        ColorSamples[4] = Color.YELLOW;
        ColorSamples[5] = Color.PINK;
        ColorSamples[6] = Color.RED;
        ColorSamples[7] = Color.SILVER;
        ColorSamples[8] = Color.GOLD;

        for (int i = height; i > 0; i -= 50) {
            for (int j = width; j > 0; j -= 25) {
                if (spaceCheck % 2 == 0) {
                    Brick brick = new Brick(j, i, 30, 30, (int)(Math.random() * 3) + 1);
                    switch (brick.getHP()) {
                        case 1:
                            brick.getPos().setFill(ColorSamples[(int)(Math.random() * 7)]);
                            break;
                        case 3:
                            brick.getPos().setFill(ColorSamples[7]);
                            break;
                        default:
                            brick.getPos().setFill(ColorSamples[8]);
                            break;
                    }
                    scene.getChildren().add(brick.getPos());
                    bricks.add(brick);
                }
                spaceCheck++;
            }
        }
    }

    public void checkCollisionBottomZone() {
        if (circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
            timeline.stop();
            for (Brick brick : bricks) {
                scene.getChildren().remove(brick.getPos());
            }
            bricks.clear();
            scene.getChildren().remove(paddle.getPos());
            startButton.setVisible(true);
        }
    }

    public void createPowerUps(Brick brick) {
        PowerUps powerUp = new PowerUps(brick.getPos().getX(), brick.getPos().getY(), brick.getPos().getWidth(), 10);
        powerUps.add(powerUp);

        scene.getChildren().add(powerUp.getPos());
    }

    public void movePowerUps() {
        for (PowerUps powerUp : powerUps) {
            powerUp.update(scene);
        }
        checkCollisionPowerUps();
    }

    public void checkCollisionPowerUps() {
        for (PowerUps powerUp : powerUps) {
            if (powerUp.getPos().getBoundsInParent().intersects(paddle.getPos().getBoundsInParent())) {
                powerUpState = powerUp.getType();
                scene.getChildren().remove(powerUp.getPos());
            }
        }
    }
}