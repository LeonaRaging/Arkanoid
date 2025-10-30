package com.arkanoid.controller;

import static com.arkanoid.ui.MainMenu.getCurrentSelection;

import com.arkanoid.Controller;
import com.arkanoid.sound.Sound;
import com.arkanoid.ui.*;
import java.io.*;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class InputHandler {

  private AnchorPane scene;
  private MainMenu mainMenu;
  private IngameMenu ingameMenu;
  private Save save;
  private Load load;
  private GameOver gameOverMenu;
  private GameStateManager stateManager;
  private UIManager uiManager;
  private Controller controller;

  public InputHandler(AnchorPane scene, MainMenu mainMenu, IngameMenu ingameMenu,
      Save save, Load load, GameOver gameOverMenu,
      GameStateManager stateManager, UIManager uiManager, Controller controller) {
    this.scene = scene;
    this.mainMenu = mainMenu;
    this.ingameMenu = ingameMenu;
    this.save = save;
    this.load = load;
    this.gameOverMenu = gameOverMenu;
    this.stateManager = stateManager;
    this.uiManager = uiManager;
    this.controller = controller;
  }

  public void handleMainMenu() throws FileNotFoundException {
    mainMenu.update();
    if (Controller.pressedKeys.contains(KeyCode.UP)) {
      mainMenu.moveSelector(-1);
      Controller.pressedKeys.remove(KeyCode.UP);
    }

    if (Controller.pressedKeys.contains(KeyCode.DOWN)) {
      mainMenu.moveSelector(1);
      Controller.pressedKeys.remove(KeyCode.DOWN);
    }

    if (Controller.pressedKeys.contains(KeyCode.ENTER)) {
      switch (getCurrentSelection()) {
        case 0:
          stateManager.setCurrentState(GameStateManager.State.OPENING);
          Sound.playOpeningVideo(scene);
          break;
        case 1:
          uiManager.startLoad();
          break;
        case 2:
          uiManager.startScoreBoard();
          break;
        default:
          break;
      }
      Controller.pressedKeys.remove(KeyCode.ENTER);
    }

    if (Controller.pressedKeys.contains(KeyCode.ESCAPE)) {
      uiManager.startMainMenu();
    }
  }

  public void handleIngameMenu() throws IOException {
    ingameMenu.update();
    if (Controller.pressedKeys.contains(KeyCode.UP)) {
      ingameMenu.moveSelector(-1);
      Controller.pressedKeys.remove(KeyCode.UP);
    }

    if (Controller.pressedKeys.contains(KeyCode.DOWN)) {
      ingameMenu.moveSelector(1);
      Controller.pressedKeys.remove(KeyCode.DOWN);
    }

    if (Controller.pressedKeys.contains(KeyCode.ENTER)) {
      switch (getCurrentSelection()) {
        case 0:
          uiManager.resumeGame();
          break;
        case 1:
          uiManager.startSave();
          break;
        case 2:
          controller.gameOver();
          uiManager.startMainMenu();
          break;
        default:
          break;
      }
      Controller.pressedKeys.remove(KeyCode.ENTER);
    }

    if (Controller.pressedKeys.contains(KeyCode.ESCAPE)) {
      uiManager.startIngameMenu();
      Controller.pressedKeys.remove(KeyCode.ESCAPE);
    }
  }

  public void handleSave() throws IOException {
    save.update();
    if (Controller.pressedKeys.contains(KeyCode.UP)) {
      save.moveSelector(-1);
      Controller.pressedKeys.remove(KeyCode.UP);
    }
    if (Controller.pressedKeys.contains(KeyCode.DOWN)) {
      save.moveSelector(1);
      Controller.pressedKeys.remove(KeyCode.DOWN);
    }
    if (Controller.pressedKeys.contains(KeyCode.ENTER)) {
      File file = new File(
          "src/main/resources/com/arkanoid/ui/save" + getCurrentSelection() + ".txt");
      FileWriter fileWriter = new FileWriter(file);
      try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        bufferedWriter.append(Integer.toString(Controller.getLevel()));
        bufferedWriter.newLine();
        bufferedWriter.append(Integer.toString(controller.getInitialScore()));
      }

      uiManager.startIngameMenu();
      Controller.pressedKeys.remove(KeyCode.ENTER);
    }

    if (Controller.pressedKeys.contains(KeyCode.ESCAPE)) {
      uiManager.startIngameMenu();
      Controller.pressedKeys.remove(KeyCode.ESCAPE);
    }
  }

  public void handleLoad() throws IOException {
    load.update();
    if (Controller.pressedKeys.contains(KeyCode.UP)) {
      load.moveSelector(-1);
      Controller.pressedKeys.remove(KeyCode.UP);
    }
    if (Controller.pressedKeys.contains(KeyCode.DOWN)) {
      load.moveSelector(1);
      Controller.pressedKeys.remove(KeyCode.DOWN);
    }
    if (Controller.pressedKeys.contains(KeyCode.ENTER)) {
      File file = new File(
          "src/main/resources/com/arkanoid/ui/save" + getCurrentSelection() + ".txt");
      Scanner sc = new Scanner(file);
      int loadedLevel = sc.nextInt();
      int loadedScore = sc.nextInt();
      sc.close();
      controller.startGameButtonAction(new ActionEvent(), loadedLevel);
      stateManager.goReadyState();
      Controller.score.setScore(loadedScore);
      controller.setInitialScore(loadedScore);
      Controller.pressedKeys.remove(KeyCode.ENTER);
    }

    if (Controller.pressedKeys.contains(KeyCode.ESCAPE)) {
      uiManager.startMainMenu();
      Controller.pressedKeys.remove(KeyCode.ESCAPE);
    }
  }

  public void handleGameOver() throws FileNotFoundException {
    gameOverMenu.update();
    if (Controller.pressedKeys.contains(KeyCode.UP)) {
      gameOverMenu.moveSelector(-1);
      Controller.pressedKeys.remove(KeyCode.UP);
    }

    if (Controller.pressedKeys.contains(KeyCode.DOWN)) {
      gameOverMenu.moveSelector(1);
      Controller.pressedKeys.remove(KeyCode.DOWN);
    }

    if (Controller.pressedKeys.contains(KeyCode.ENTER)) {
      switch (getCurrentSelection()) {
        case 0:
          controller.startGameButtonAction(new ActionEvent(), Controller.getLevel());
          stateManager.goReadyState();
          break;
        case 1:
          uiManager.startMainMenu();
          stateManager.setCurrentState(GameStateManager.State.MENU);
          break;
        default:
          break;
      }
      Controller.pressedKeys.remove(KeyCode.ENTER);
    }
  }
}

