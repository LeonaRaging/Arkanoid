package com.arkanoid.controller;

import com.arkanoid.Controller;
import com.arkanoid.background.BlackChange;
import com.arkanoid.ui.*;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;

public class UIManager {

    private AnchorPane scene;
    private MainMenu mainMenu;
    private ImageView startBackground;
    private ImageView backgroundView;
    private ImageView backgroundView11;
    private ImageView backgroundViewother;
    private ImageView scoreBoardView;
    private ImageView gameOverScreen;
    private ScoreBoard scoreBoard;
    private IngameMenu ingameMenu;
    private Save save;
    private Load load;
    private GameOver gameOverMenu;
    private GameStateManager stateManager;
    private BlackChange black;

    public UIManager(AnchorPane scene, MainMenu mainMenu, ImageView startBackground,
                    ImageView backgroundView, ImageView backgroundView11, ImageView backgroundViewother,
                    ImageView scoreBoardView, ImageView gameOverScreen, ScoreBoard scoreBoard,
                    IngameMenu ingameMenu, Save save, Load load, GameOver gameOverMenu) {
        this.scene = scene;
        this.mainMenu = mainMenu;
        this.startBackground = startBackground;
        this.backgroundView = backgroundView;
        this.backgroundView11 = backgroundView11;
        this.backgroundViewother = backgroundViewother;
        this.scoreBoardView = scoreBoardView;
        this.gameOverScreen = gameOverScreen;
        this.scoreBoard = scoreBoard;
        this.ingameMenu = ingameMenu;
        this.save = save;
        this.load = load;
        this.gameOverMenu = gameOverMenu;
    }

    public void setStateManager(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setBlackChange(BlackChange black) {
        this.black = black;
    }

    public void resetAnchorPane() {
        for (Node node : scene.getChildren()) {
            node.setVisible(false);
        }
    }

    public void startMainMenu() {
        stateManager.setCurrentState(GameStateManager.State.MENU);
        resetAnchorPane();
        mainMenu.setVisible(true);
        startBackground.setVisible(true);
    }

    public void startIngameMenu() {
        stateManager.setCurrentState(GameStateManager.State.INGAMEMENU);
        resetAnchorPane();
        ingameMenu.setVisible(true);
        startBackground.setVisible(true);
    }

    public void startScoreBoard() throws FileNotFoundException {
        resetAnchorPane();
        scoreBoard.setScore();
        scoreBoard.setVisible(true);
        scoreBoardView.setVisible(true);
    }

    public void startSave() {
        stateManager.setCurrentState(GameStateManager.State.SAVE);
        resetAnchorPane();
        save.setVisible(true);
        startBackground.setVisible(true);
    }

    public void startLoad() {
        stateManager.setCurrentState(GameStateManager.State.LOAD);
        resetAnchorPane();
        load.setVisible(true);
        startBackground.setVisible(true);
    }

    public void startGameOver() {
        stateManager.setCurrentState(GameStateManager.State.GAMEOVER);
        resetAnchorPane();
        gameOverMenu.setVisible(true);
        gameOverScreen.setVisible(true);
    }

    public void resumeGame() {
        resetAnchorPane();
        for (Node node : scene.getChildren()) {
            if (node != mainMenu && node != scoreBoardView && node != scoreBoard
                && node != backgroundView11 && node != backgroundViewother
                && node != save && node != load && node != ingameMenu
                && node != gameOverScreen && node != gameOverMenu
                && node != startBackground && (black != null && node != black.getImageView())) {
                node.setVisible(true);
            }
        }

        int level = Controller.getLevel();
        if (level < 5) {
            backgroundView.setVisible(true);
        } else if (level == 5 || level == 15) {
            backgroundView11.setVisible(true);
        } else {
            backgroundViewother.setVisible(true);
        }
        stateManager.setCurrentState(GameStateManager.State.RUNNING);
    }

    public void updateBackgroundForLevel(int level) {
        backgroundView.setVisible(false);
        backgroundView11.setVisible(false);
        backgroundViewother.setVisible(false);

        if (level <= 5) {
            backgroundView.setVisible(true);
        } else if (level == 5 || level == 15) {
            backgroundView11.setVisible(true);
        } else {
            backgroundViewother.setVisible(true);
        }
    }
}
