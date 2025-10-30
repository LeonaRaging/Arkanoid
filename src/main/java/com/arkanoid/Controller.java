package com.arkanoid;

import com.arkanoid.background.BlackChange;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.controller.GameLifecycleManager;
import com.arkanoid.controller.GameStateManager;
import com.arkanoid.controller.InputHandler;
import com.arkanoid.controller.UIManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import com.arkanoid.core.Paddle;
import com.arkanoid.enemies.EnemiesManager;
import com.arkanoid.field.Field;
import com.arkanoid.field.Gate;
import com.arkanoid.numberandstringdisplay.*;
import com.arkanoid.powerup.PowerUp;
import com.arkanoid.powerup.PowerUpManager;
import com.arkanoid.sound.Sound;
import com.arkanoid.ui.GameOver;
import com.arkanoid.ui.IngameMenu;
import com.arkanoid.ui.Load;
import com.arkanoid.ui.MainMenu;
import com.arkanoid.ui.Save;
import com.arkanoid.ui.ScoreBoard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Controller implements Initializable {

  @FXML
  private AnchorPane scene;

  public static Paddle paddle;
  public static Rectangle bottomZone;
  public static Field field;
  public static Field outline;
  public static Gate[] gates = new Gate[4];

  public static ScoreDisplay score;
  private LevelDisplay lv;
  private Hp hp;

  private int initialScore = 0;

  @FXML
  private MainMenu mainMenu;
  @FXML
  private ImageView startBackground;
  @FXML
  private ImageView backgroundView;
  @FXML
  private ImageView backgroundView11;
  @FXML
  private ImageView backgroundViewother;
  @FXML
  private ImageView scoreBoardView;
  @FXML
  private ImageView gameOverScreen;
  @FXML
  private ScoreBoard scoreBoard;
  @FXML
  private IngameMenu ingameMenu;
  @FXML
  private Save save;
  @FXML
  private Load load;
  @FXML
  private GameOver gameOverMenu;

  public static final Set<KeyCode> pressedKeys = new HashSet<>();

  public BlackChange black = new BlackChange(0, 0, 256, 224);
  private StringDisplay round;
  private StringDisplay highScore;
  private StringDisplay HighScore;
  private ScoreDisplay maxscore;

  // Sub-managers
  private GameStateManager stateManager;
  private InputHandler inputHandler;
  private GameLifecycleManager lifecycleManager;
  private UIManager uiManager;

  Timeline timeline = new Timeline(
      new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          try {
            switch (stateManager.getCurrentState()) {
              case MENU:
                inputHandler.handleMainMenu();
                break;

              case READY:
                if (pressedKeys.contains(KeyCode.ENTER) && !Sound.isEndLevelPlaying()) {
                  stateManager.startPlay();
                  int level = getLevel();
                  if (level == 5) {
                    Sound.playGiantCentipedeMusic();
                  }
                  if (level == 15) {
                    Sound.playDohFaceMusic();
                  }
                }
                break;

              case PADDLE_APPEARING:
                stateManager.handlePaddleAppearing();
                break;

              case PADDLE_BREAKING:
                stateManager.handlePaddleBreaking();
                break;

              case RUNNING:
                if (stateManager.getLastTime() == 0) {
                  stateManager.resetLastTime();
                }
                stateManager.handleIngameUpdate();
                break;

              case INGAMEMENU:
                inputHandler.handleIngameMenu();
                break;

              case SAVE:
                inputHandler.handleSave();
                break;

              case LOAD:
                inputHandler.handleLoad();
                break;

              case PRE_NEWLEVEL:
                stateManager.handlePreNewLevel();
                break;

              case GAMEOVER:
                inputHandler.handleGameOver();
                break;

              case ENDING:
                stateManager.handleEnding();
                break;

              case OPENING:
                stateManager.handleOpening();
                break;

              default:
                break;
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Initialize sub-managers
    lifecycleManager = new GameLifecycleManager(scene);
    uiManager = new UIManager(scene, mainMenu, startBackground, backgroundView,
                             backgroundView11, backgroundViewother, scoreBoardView,
                             gameOverScreen, scoreBoard, ingameMenu, save, load, gameOverMenu);
    stateManager = new GameStateManager(scene, this, lifecycleManager, uiManager, black);

    uiManager.setStateManager(stateManager);
    uiManager.setBlackChange(black);
    lifecycleManager.setStateManager(stateManager);
    lifecycleManager.setUIManager(uiManager);

    inputHandler = new InputHandler(scene, mainMenu, ingameMenu, save, load,
                                   gameOverMenu, stateManager, uiManager, this);

    timeline.setCycleCount(Timeline.INDEFINITE);
    mainMenu.setChoices();
    ingameMenu.setChoices();
    save.setChoices();
    load.setChoices();
    gameOverMenu.setChoices();

    scene.getChildren().add(black.getImageView());

    scene.setFocusTraversable(true);
    scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
    scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
    scene.requestFocus();

    uiManager.startMainMenu();

    timeline.play();
  }

  @FXML
  public void startGameButtonAction(ActionEvent event, int Level) throws FileNotFoundException {
    stateManager.setGameOver(false);
    uiManager.resetAnchorPane();

    if (Level < 5) {
      backgroundView.setVisible(true);
    } else if (Level == 5 || Level == 15) {
      backgroundView11.setVisible(true);
    } else {
      backgroundViewother.setVisible(true);
    }

    Hp.resetHp();

    field = new Field(16, 16, 160, 208, Integer.toString(Level));
    outline = new Field(8, 8, 176, 216, "outline");
    gates[0] = new Gate(32, 8, 32, 8, "gate0");
    gates[1] = new Gate(128, 8, 32, 8, "gate0");
    gates[2] = new Gate(8, 47, 8, 30, "gate1");
    gates[3] = new Gate(176, 47, 8, 30, "gate1");

    scene.getChildren().add(field.getImageView());
    scene.getChildren().add(outline.getImageView());

    for (int i = 0; i < 4; i++) {
      scene.getChildren().add(gates[i].getImageView());
    }

    paddle = new Paddle(112, 210, 32, 8);
    scene.getChildren().add(paddle.getImageView());

    lifecycleManager.newLife();

    lifecycleManager.setLevel(Level);
    Hp.resetHp();

    lifecycleManager.newLife();

    BrickManager.createBricks(scene, Level);

    File file = new File("src/main/resources/com/arkanoid/ui/score.txt");
    Scanner sc = new Scanner(file);
    int highscore = 0;
    while (sc.hasNextInt()) {
      int temp = sc.nextInt();
      if (temp > highscore) {
        highscore = temp;
      }
    }

    score = new ScoreDisplay(200, 50, 0);
    maxscore = new ScoreDisplay(200, 28, highscore);
    round = new StringDisplay("ROUND", 200, 160, true);
    highScore = new StringDisplay("SCORE", 210, 20, true);
    HighScore = new StringDisplay("HIGH", 200, 12, true);
    score.showScore(scene);
    round.show(scene);
    highScore.show(scene);
    HighScore.show(scene);
    maxscore.showScore(scene);
    lv = new LevelDisplay(Level);
    lv.showLevel(scene);
    hp = new Hp(scene);

    lifecycleManager.setLevelDisplay(lv);
    lifecycleManager.setHp(hp);

    EnemiesManager.isGameOver();

    bottomZone = new Rectangle(0, 220, 256, 10);
  }

  public void gameOver() throws IOException {
    lifecycleManager.gameOver();

    round.clear(scene);
    highScore.clear(scene);
    HighScore.clear(scene);
    maxscore.clear(scene);
  }

  public void startPaddleBreaking() {
    Sound.stopGiantCentipedeMusic();
    Sound.stopDohFaceMusic();

    Hp.loseLife();
    hp.updateDisplay();

    for (Ball ball : BallManager.getBalls()) {
      scene.getChildren().remove(ball.getImageView());
      for (int i = 0; i < 6; i++) {
        scene.getChildren().remove(ball.getImageViews()[i]);
      }
    }
    BallManager.getBalls().clear();

    for (PowerUp powerUp : PowerUpManager.getPowerUps()) {
      scene.getChildren().remove(powerUp.getImageView());
    }
    for (Entity projectile : PowerUpManager.getProjectiles()) {
      scene.getChildren().remove(projectile.getImageView());
    }
    PowerUpManager.resetPower();

    paddle.startBreakAnimation();
    stateManager.setCurrentState(GameStateManager.State.PADDLE_BREAKING);
    Sound.playDead();
  }

  public static int getLevel() {
    return GameLifecycleManager.getLevel() != 0 ? GameLifecycleManager.getLevel() : 1;
  }

  public int getInitialScore() {
    return initialScore;
  }

  public void setInitialScore(int score) {
    this.initialScore = score;
  }
}
