package com.arkanoid;

import com.arkanoid.background.BlackChange;
import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;
import com.arkanoid.core.Entity;
import com.arkanoid.core.Paddle;
import com.arkanoid.enemies.Enemies;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import javafx.scene.Node;
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
  private static Field outline;
  public static Gate[] gates = new Gate[4];

  private ScoreDisplay score;
  private LevelDisplay lv;
  private Hp hp;

  private static int level;

  private boolean isGameOver;

  private enum State {
    MENU, READY, PADDLE_APPEARING, PADDLE_BREAKING, RUNNING, INGAMEMENU, SAVE, LOAD, PRE_NEWLEVEL, GAMEOVER
  }

  private State currentState;
  private StringDisplay player = new StringDisplay("PLAYER", 64, 150, false);
  private Digit number1 = new Digit(120, 150, 8, 8, 1);
  private StringDisplay ready = new StringDisplay("READY", 76, 166, false);
  private BlackChange black = new BlackChange(0, 0, 256, 224);

  private StringDisplay round = new StringDisplay("ROUND", 200,160, true);
  private StringDisplay highScore = new StringDisplay("SCORE", 210,20, true);
  private StringDisplay HighScore = new StringDisplay("HIGH", 200, 12, true);
  private ScoreDisplay maxscore;

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
  long lastTime;

  private void goBlack() {
    currentState = State.PRE_NEWLEVEL;
    black.startAsc();
  }

  private void goReadyState() {
    currentState = State.READY;
    player.show(scene);
    number1.display(scene);
    ready.show(scene);
    black.newLevel();
  }

  private void startPlay() {
    player.clear(scene);
    number1.undisplay(scene);
    ready.clear(scene);
    currentState = State.PADDLE_APPEARING;
    pressedKeys.remove(KeyCode.ENTER);
  }

  Timeline timeline = new Timeline(
      new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        private void handleIngame() throws IOException {
          if (pressedKeys.contains(KeyCode.ESCAPE)) {
            startIngameMenu();
            return;
          }

          paddle.update(field.getRectangle());

          for (Ball ball : BallManager.getBalls()) {
            paddle.checkCollisionPaddle(ball);
            ball.update(scene);
          }

          long currentTime = System.nanoTime();
          double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
          lastTime = currentTime;

          EnemiesManager.update(scene);
          EnemiesManager.updateEnemies(scene, deltaTime);

          brickUpdate();

          if (currentState == State.PRE_NEWLEVEL) {
            return;
          }

          powerUpUpdate();

          ballUpdate();

          gateUpdate();

          if (BallManager.checkCollisionBottomZone(scene)
                || EnemiesManager.isGameOver()) {
            EnemiesManager.setGameOver(false);
            startPaddleBreaking();
          }

          if (score != null) {
            score.reup();
          }
        }

        private void handleMainMenu() throws FileNotFoundException {
          mainMenu.update();
          if (pressedKeys.contains(KeyCode.UP)) {
            mainMenu.moveSelector(-1);
            pressedKeys.remove(KeyCode.UP);
          }

          if (pressedKeys.contains(KeyCode.DOWN)) {
            mainMenu.moveSelector(1);
            pressedKeys.remove(KeyCode.DOWN);
          }

          if (pressedKeys.contains(KeyCode.ENTER)) {
            switch (MainMenu.getCurrentSelection()) {
              case 0:
                startGameButtonAction(new ActionEvent(), 1);
                goReadyState();
                pressedKeys.remove(KeyCode.ENTER);
                break;
              case 1:
                startLoad();
                break;
              case 2:
                startScoreBoard();
                break;
              default:
                break;
            }
            pressedKeys.remove(KeyCode.ENTER);
          }

          if (pressedKeys.contains(KeyCode.ESCAPE)) {
            startMainMenu();
          }
        }

        private void handleIngameMenu() throws IOException {
          ingameMenu.update();
          if (pressedKeys.contains(KeyCode.UP)) {
            ingameMenu.moveSelector(-1);
            pressedKeys.remove(KeyCode.UP);
          }

          if (pressedKeys.contains(KeyCode.DOWN)) {
            ingameMenu.moveSelector(1);
            pressedKeys.remove(KeyCode.DOWN);
          }

          if (pressedKeys.contains(KeyCode.ENTER)) {
            switch (ingameMenu.getCurrentSelection()) {
              case 0:
                resetAnchorPane();
                for (Node node : scene.getChildren()) {
                  if (node != mainMenu && node != scoreBoardView && node != scoreBoard
                      && node != backgroundView11 && node != backgroundViewother
                      && node != save && node != load && node != ingameMenu
                      && node != gameOverScreen && node != gameOverMenu) {
                    node.setVisible(true);
                  }
                }
                if (level < 5) {
                  backgroundView.setVisible(true);
                } else if (level == 5) {
                  backgroundView11.setVisible(true);
                } else {
                  backgroundViewother.setVisible(true);
                }
                currentState = State.RUNNING;
                break;
              case 1:
                startSave();
                break;
              case 2:
                gameOver();
                startMainMenu();
                break;
              default:
                break;
            }
            pressedKeys.remove(KeyCode.ENTER);
          }

          if (pressedKeys.contains(KeyCode.ESCAPE)) {
            startIngameMenu();
            pressedKeys.remove(KeyCode.ESCAPE);
          }
        }

        private void handleSave() throws IOException {
          save.update();
          if (pressedKeys.contains(KeyCode.UP)) {
            save.moveSelector(-1);
            pressedKeys.remove(KeyCode.UP);
          }
          if (pressedKeys.contains(KeyCode.DOWN)) {
            save.moveSelector(1);
            pressedKeys.remove(KeyCode.DOWN);
          }
          if (pressedKeys.contains(KeyCode.ENTER)) {
            File file = new File(
                "src/main/resources/com/arkanoid/ui/save" + Save.getCurrentSelection() + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(Integer.toString(level));
            bufferedWriter.newLine();
            bufferedWriter.append(Integer.toString(initialScore));
            bufferedWriter.close();
            fileWriter.close();

            startIngameMenu();
            pressedKeys.remove(KeyCode.ENTER);
          }

          if (pressedKeys.contains(KeyCode.ESCAPE)) {
            startIngameMenu();
            pressedKeys.remove(KeyCode.ESCAPE);
          }
        }

        private void handleLoad() throws IOException {
          load.update();
          if (pressedKeys.contains(KeyCode.UP)) {
            load.moveSelector(-1);
            pressedKeys.remove(KeyCode.UP);
          }
          if (pressedKeys.contains(KeyCode.DOWN)) {
            load.moveSelector(1);
            pressedKeys.remove(KeyCode.DOWN);
          }
          if (pressedKeys.contains(KeyCode.ENTER)) {
            File file = new File(
                "src/main/resources/com/arkanoid/ui/save" + Load.getCurrentSelection() + ".txt");
            Scanner sc = new Scanner(file);
            int loadedLevel = sc.nextInt();
            int loadedScore = sc.nextInt();
            sc.close();
            startGameButtonAction(new ActionEvent(), loadedLevel);
            goReadyState();
            ScoreDisplay.setScore(loadedScore);
            initialScore = loadedScore;
            pressedKeys.remove(KeyCode.ENTER);
          }

          if (pressedKeys.contains(KeyCode.ESCAPE)) {
            startMainMenu();
            pressedKeys.remove(KeyCode.ESCAPE);
          }
        }

        private void handleGameOver() throws FileNotFoundException {
          gameOverMenu.update();
          if (pressedKeys.contains(KeyCode.UP)) {
            gameOverMenu.moveSelector(-1);
            pressedKeys.remove(KeyCode.UP);
          }

          if (pressedKeys.contains(KeyCode.DOWN)) {
            gameOverMenu.moveSelector(1);
            pressedKeys.remove(KeyCode.DOWN);
          }

          if (pressedKeys.contains(KeyCode.ENTER)) {
            switch (gameOverMenu.getCurrentSelection()) {
              case 0:
                startGameButtonAction(new ActionEvent(), level);
                goReadyState();
                break;
              case 1:
                startMainMenu();
                currentState = State.MENU;
                break;
              default:
                break;
            }
            pressedKeys.remove(KeyCode.ENTER);
          }
        }

        @Override
        public void handle(ActionEvent actionEvent) {
          try {
            switch (currentState) {
              case MENU:
                handleMainMenu();
                break;

              case READY:
                if (pressedKeys.contains(KeyCode.ENTER) && !Sound.isEndLevelPlaying()) {
                  startPlay();
                  if (level == 5) {
                    Sound.playGiantCentipedeMusic();
                  }
                  if (level == 15) {
                    Sound.playDohFaceMusic();
                  }
                }
                break;

              case PADDLE_APPEARING:
                boolean isAnimating = paddle.updateAppearAnimation();
                if (!isAnimating) {
                  currentState = State.RUNNING;
                  lastTime = System.nanoTime();
                }
                break;

              case PADDLE_BREAKING:
                boolean isBreaking = paddle.updateBreakAnimation();
                if (!isBreaking) {
                  if (Hp.getHp() <= 0) {
                    goBlack();
                    isGameOver = true;
                  } else {
                    newLife();
                    goReadyState();
                  }
                }
                break;

              case RUNNING:
                if (lastTime == 0) {
                  lastTime = System.nanoTime();
                }
                handleIngame();
                break;
              case INGAMEMENU:
                handleIngameMenu();
                break;
              case SAVE:
                handleSave();
                break;
              case LOAD:
                handleLoad();
                break;
              case PRE_NEWLEVEL:
                boolean stillBlack = black.updateAsc();
                if (stillBlack == false) {
                  if (isGameOver) {
                    startGameOver();
                    Sound.playGameOver();
                    gameOver();
                  } else {
                    if (level == 5) {
                      Sound.stopGiantCentipedeMusic();
                    }
                    if (level == 15) {
                      Sound.stopDohFaceMusic();
                    }
                    level++;
                    if (level > 15) {
                      gameOver();
                      startMainMenu();
                    }

                    if (level == 11) {
                      backgroundView.setVisible(false);
                      backgroundView11.setVisible(true);
                    }
                    if (level == 12) {
                      backgroundView11.setVisible(false);
                      backgroundViewother.setVisible(true);
                    }
                    BallManager.isCaught = 0;
                    field.changeField(level);
                    newLife();
                    for (Brick brick : BrickManager.getBricks()) {
                      scene.getChildren().remove(brick.getImageView());
                      scene.getChildren().remove(brick.shadow);
                    }
                    BrickManager.getBricks().clear();
                    BrickManager.createBricks(scene, level);
                    initialScore = score.getScore();
                    goReadyState();
                  }
                }
                break;
              case GAMEOVER:
                handleGameOver();
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
    startMainMenu();
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

    startMainMenu();

    timeline.play();
  }

  @FXML
  void startGameButtonAction(ActionEvent event, int Level) throws FileNotFoundException {
    isGameOver = false;
    resetAnchorPane();
    startBackground.setVisible(false);
    if (Level < 5) {
      backgroundView.setVisible(true);
    } else if (Level == 5 || Level == 15) {
      backgroundView11.setVisible(true);
    } else {
      backgroundViewother.setVisible(true);
    }

    ScoreDisplay.setScore(0);
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

    newLife();

    level = Level;
    ScoreDisplay.setScore(0);
    Hp.resetHp();

    newLife();

    BrickManager.createBricks(scene, level);

    score = new ScoreDisplay(200, 50, 0);
    maxscore = new ScoreDisplay(200, 28, 0);
    round = new StringDisplay("ROUND", 200,160, true);
    highScore = new StringDisplay("SCORE", 210,20, true);
    HighScore = new StringDisplay("HIGH", 200, 12, true);
    score.showScore(scene);
    round.show(scene);
    highScore.show(scene);
    HighScore.show(scene);
    maxscore.showScore(scene);
    lv = new LevelDisplay(level);
    lv.showLevel(scene);
    hp = new Hp(scene);
    EnemiesManager.isGameOver();

    bottomZone = new Rectangle(0, 220, 256, 10);
  }

  private void newLife() {
    for (Ball ball : BallManager.getBalls()) {
      scene.getChildren().remove(ball.getImageView());
    }
    BallManager.getBalls().clear();

    paddle.getRectangle().setX(112);
    paddle.getRectangle().setY(210);
    paddle.setState(0);
    paddle.getRectangle().setWidth(32);

    paddle.resetAppearAnimation();

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
    scene.getChildren().remove(paddle.getImageView());
    scene.getChildren().remove(field.getImageView());
    scene.getChildren().remove(outline.getImageView());
    //lv.clear(scene);
    player.clear(scene);
    for (int i = 0; i < 4; i++) {
      scene.getChildren().remove(gates[i].getImageView());
    }

    EnemiesManager.clear(scene);

    /*
    clear hp and score
     */
    score.clear(scene);
    highScore.clear(scene);
    HighScore.clear(scene);
    maxscore.clear(scene);
    round.clear(scene);
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
    bufferedWriter.append(Integer.toString(score.getScore()));
    bufferedWriter.close();
    fileWriter.close();
  }

  private void brickUpdate() throws IOException {
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
      goBlack();
    }
  }

  private void powerUpUpdate() {
    PowerUpManager.movePowerUps(scene);
    PowerUpManager.checkCollisionPowerUps(paddle, scene);
    PowerUpManager.update(paddle, scene);
    BallManager.checkCollisionScene(field.getRectangle());
  }

  private void ballUpdate() {
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

  private void gateUpdate() {
    for (int i = 0; i < 4; i++) {
      gates[i].update(i);
    }
  }

  private void resetAnchorPane() {
    for (Node node : scene.getChildren()) {
      node.setVisible(false);
    }
  }

  private void startMainMenu() {
    currentState = State.MENU;
    resetAnchorPane();
    mainMenu.setVisible(true);
    startBackground.setVisible(true);
  }

  private void startIngameMenu() {
    currentState = State.INGAMEMENU;
    resetAnchorPane();
    ingameMenu.setVisible(true);
    startBackground.setVisible(true);
  }

  private void startScoreBoard() throws FileNotFoundException {
    resetAnchorPane();
    scoreBoard.setScore();
    scoreBoard.setVisible(true);
    scoreBoardView.setVisible(true);
  }

  private void startSave() {
    currentState = State.SAVE;
    resetAnchorPane();
    save.setVisible(true);
    startBackground.setVisible(true);
  }

  private void startLoad() {
    currentState = State.LOAD;
    resetAnchorPane();
    load.setVisible(true);
    startBackground.setVisible(true);
  }

  private void startGameOver() {
    currentState = State.GAMEOVER;
    resetAnchorPane();
    gameOverMenu.setVisible(true);
    gameOverScreen.setVisible(true);
  }

  public void startPaddleBreaking() {
      Sound.stopGiantCentipedeMusic();
      Sound.stopDohFaceMusic();

      Hp.loseLife();
      hp.updateDisplay();

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
      for (Entity projectile : PowerUpManager.getProjectiles()) {
          scene.getChildren().remove(projectile.getImageView());
      }
      PowerUpManager.resetPower();

      paddle.startBreakAnimation();
      currentState = State.PADDLE_BREAKING;
      Sound.playDead();
  }

  public static int getLevel() {
    return level;
  }
}
