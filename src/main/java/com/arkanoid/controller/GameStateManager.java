package com.arkanoid.controller;

import com.arkanoid.Controller;
import com.arkanoid.background.BlackChange;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.numberandstringdisplay.*;
import com.arkanoid.sound.Sound;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class GameStateManager {

    public enum State {
        MENU, READY, PADDLE_APPEARING, PADDLE_BREAKING, RUNNING, INGAMEMENU,
        SAVE, LOAD, PRE_NEWLEVEL, GAMEOVER, OPENING, ENDING
    }

    private State currentState;
    private AnchorPane scene;
    private Controller controller;
    private GameLifecycleManager lifecycleManager;
    private UIManager uiManager;
    private BlackChange black;
    private boolean isGameOver;
    private long lastTime;

    private StringDisplay player;
    private Digit number1;
    private StringDisplay ready;

    public GameStateManager(AnchorPane scene, Controller controller,
                           GameLifecycleManager lifecycleManager, UIManager uiManager,
                           BlackChange black) {
        this.scene = scene;
        this.controller = controller;
        this.lifecycleManager = lifecycleManager;
        this.uiManager = uiManager;
        this.black = black;
        this.currentState = State.MENU;
        this.player = new StringDisplay("PLAYER", 64, 150, false);
        this.number1 = new Digit(120, 150, 8, 8, 1);
        this.ready = new StringDisplay("READY", 76, 166, false);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public void goBlack() {
        currentState = State.PRE_NEWLEVEL;
        black.startAsc();
    }

    public void goReadyState() {
        currentState = State.READY;
        player.show(scene);
        number1.display(scene);
        ready.show(scene);
        black.newLevel();
    }

    public void startPlay() {
        player.clear(scene);
        number1.undisplay(scene);
        ready.clear(scene);
        currentState = State.PADDLE_APPEARING;
        Controller.pressedKeys.remove(KeyCode.ENTER);
    }

    public void handleIngameUpdate() throws IOException {
        if (Controller.pressedKeys.contains(KeyCode.ESCAPE)) {
            uiManager.startIngameMenu();
            return;
        }

        Controller.paddle.update(Controller.field.getRectangle());

        for (Ball ball : BallManager.getBalls()) {
            Controller.paddle.checkCollisionPaddle(ball);
            ball.update(scene);
        }

        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
        lastTime = currentTime;

        EnemiesManager.update(scene);
        EnemiesManager.updateEnemies(scene, deltaTime);

        lifecycleManager.brickUpdate();

        if (currentState == State.PRE_NEWLEVEL) {
            return;
        }

        lifecycleManager.powerUpUpdate();
        lifecycleManager.ballUpdate();
        lifecycleManager.gateUpdate();

        if (BallManager.checkCollisionBottomZone(scene) || EnemiesManager.isGameOver()) {
            EnemiesManager.setGameOver(false);
            controller.startPaddleBreaking();
        }

        if (Controller.score != null) {
            Controller.score.reup();
        }
    }

    public void handlePaddleAppearing() {
        boolean isAnimating = Controller.paddle.updateAppearAnimation();
        if (!isAnimating) {
            currentState = State.RUNNING;
            lastTime = System.nanoTime();
        }
    }

    public void handlePaddleBreaking() {
        boolean isBreaking = Controller.paddle.updateBreakAnimation();
        if (!isBreaking) {
            if (Hp.getHp() <= 0) {
                goBlack();
                isGameOver = true;
            } else {
                lifecycleManager.newLife();
                goReadyState();
            }
        }
    }

    public void handlePreNewLevel() throws IOException {
        boolean stillBlack = black.updateAsc();
        if (!stillBlack) {
            if (isGameOver) {
                uiManager.startGameOver();
                Sound.playGameOver();
                lifecycleManager.gameOver();
            } else {
                lifecycleManager.advanceLevel();
            }
        }
    }

    public void handleOpening() {
        if (!Sound.isOpeningVideoPlaying() || Controller.pressedKeys.contains(KeyCode.ENTER)) {
            try {
                controller.startGameButtonAction(null, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            goReadyState();
            currentState = State.READY;
            Controller.pressedKeys.remove(KeyCode.ENTER);
            Sound.stopOpeningVideo(scene);
        }
    }

    public void handleEnding() {
        if (!Sound.isEndingVideoPlaying() || Controller.pressedKeys.contains(KeyCode.ENTER)) {
            uiManager.startMainMenu();
            currentState = State.MENU;
            Controller.pressedKeys.remove(KeyCode.ENTER);
            Sound.stopEndingVideo(scene);
        }
    }

    public void resetLastTime() {
        this.lastTime = 0;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}

