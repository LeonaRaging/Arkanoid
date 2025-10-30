package com.arkanoid.controller;

import com.arkanoid.Controller;
import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.numberandstringdisplay.*;
import com.arkanoid.powerup.PowerUp;
import com.arkanoid.powerup.PowerUpManager;
import com.arkanoid.sound.Sound;
import javafx.scene.layout.AnchorPane;
import java.io.*;

public class GameLifecycleManager {

    private AnchorPane scene;
    private GameStateManager stateManager;
    private UIManager uiManager;
    private LevelDisplay lv;
    private Hp hp;
    private static int level;

    public GameLifecycleManager(AnchorPane scene) {
        this.scene = scene;
    }

    public void setStateManager(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public void setLevelDisplay(LevelDisplay lv) {
        this.lv = lv;
    }

    public void setHp(Hp hp) {
        this.hp = hp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static int getLevel() {
        return level;
    }

    public void newLife() {
        for (Ball ball : BallManager.getBalls()) {
            scene.getChildren().remove(ball.getImageView());
        }
        BallManager.getBalls().clear();

        Controller.paddle.getRectangle().setX(112);
        Controller.paddle.getRectangle().setY(210);
        Controller.paddle.setState(0);
        Controller.paddle.getRectangle().setWidth(32);

        Controller.paddle.resetAppearAnimation();

        Ball ball = new Ball(0, 0, 2.5);
        ball.getCircle().setLayoutX(112);
        ball.getCircle().setLayoutY(200);
        ball.setDeltaX(1);
        ball.setDeltaY(1);
        scene.getChildren().add(ball.getImageView());
        BallManager.getBalls().add(ball);

        for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
            scene.getChildren().remove(powerUp.getImageView());
        }
        for (Entity entity : PowerUpManager.getProjectiles()) {
            scene.getChildren().remove(entity.getImageView());
        }
        PowerUpManager.resetPower();
    }

    public void gameOver() throws IOException {
        for (Brick brick : BrickManager.getBricks()) {
            scene.getChildren().remove(brick.getImageView());
            scene.getChildren().remove(brick.shadow);
        }
        for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
            scene.getChildren().remove(powerUp.getImageView());
        }
        for (Entity projectile : PowerUpManager.getProjectiles()) {
            scene.getChildren().remove(projectile.getImageView());
        }
        scene.getChildren().remove(Controller.paddle.getImageView());
        scene.getChildren().remove(Controller.field.getImageView());
        scene.getChildren().remove(Controller.outline.getImageView());

        for (int i = 0; i < 4; i++) {
            scene.getChildren().remove(Controller.gates[i].getImageView());
        }

        EnemiesManager.clear(scene);

        Controller.score.clear(scene);
        lv.clear(scene);
        hp.clear(scene);

        EnemiesManager.isGameOver();
        BrickManager.getBricks().clear();
        PowerUpManager.getPowerUps().clear();
        PowerUpManager.getProjectiles().clear();

        for (Ball ball : BallManager.getBalls()) {
            scene.getChildren().remove(ball.getImageView());
            for (int i = 0; i < 6; i++) {
                scene.getChildren().remove(ball.imageViews[i]);
            }
        }
        BallManager.getBalls().clear();

        File file = new File("src/main/resources/com/arkanoid/ui/score.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append('\n');
        bufferedWriter.append(Integer.toString(Controller.score.getScore()));
        bufferedWriter.close();
        fileWriter.close();
    }

    public void brickUpdate() throws IOException {
        if (BrickManager.brickRemain > 0) {
            for (Ball ball : BallManager.getBalls()) {
                BrickManager.update(ball, scene);
            }
        } else {
            for (Ball ball : BallManager.getBalls()) {
                scene.getChildren().remove(ball.getImageView());
                for (int i = 0; i < 6; i++) {
                    scene.getChildren().remove(ball.imageViews[i]);
                }
            }
            BallManager.getBalls().clear();

            for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
                scene.getChildren().remove(powerUp.getImageView());
            }
            PowerUpManager.getPowerUps().clear();

            EnemiesManager.clear(scene);
            stateManager.goBlack();
        }
    }

    public void powerUpUpdate() {
        PowerUpManager.movePowerUps(scene);
        PowerUpManager.checkCollisionPowerUps(Controller.paddle, scene);
        PowerUpManager.update(Controller.paddle, scene);
        BallManager.checkCollisionScene(Controller.field.getRectangle());
    }

    public void ballUpdate() {
        for (Ball ball : BallManager.getBalls()) {
            if (PowerUpManager.powerUpState[0] != 1) {
                scene.getChildren().remove(ball.getImageView());
                scene.getChildren().add(ball.getImageView());
            }
        }
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

    public void gateUpdate() {
        for (int i = 0; i < 4; i++) {
            Controller.gates[i].update(i);
        }
    }

    public void advanceLevel() throws IOException {
        if (level == 5) {
            Sound.stopGiantCentipedeMusic();
        }
        if (level == 15) {
            Sound.stopDohFaceMusic();
        }

        level++;

        if (level > 15) {
            stateManager.setCurrentState(GameStateManager.State.ENDING);
            Sound.playEndingVideo(scene);
            return;
        }

        uiManager.updateBackgroundForLevel(level);

        Hp.resetHp();
        hp.updateDisplay();
        BallManager.isCaught = 0;
        Controller.field.changeField(level);
        newLife();

        for (Brick brick : BrickManager.getBricks()) {
            scene.getChildren().remove(brick.getImageView());
            scene.getChildren().remove(brick.shadow);
        }
        BrickManager.getBricks().clear();
        BrickManager.createBricks(scene, level);

        lv.clear(scene);
        lv = new LevelDisplay(level);
        lv.showLevel(scene);

        stateManager.goReadyState();
    }
}
