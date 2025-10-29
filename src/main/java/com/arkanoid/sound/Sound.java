package com.arkanoid.sound;

import javafx.animation.PauseTransition;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sound {
  private static final AudioClip bouncePaddle = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/bouncePaddle.wav").toExternalForm());
  private static final AudioClip bounceBrick = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/bounceBrick.wav").toExternalForm());
  private static final AudioClip bounceField = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/bounceField.wav").toExternalForm());
  private static final AudioClip powerUpE = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/powerUpE.wav").toExternalForm());
  private static final AudioClip Shoot = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/Shoot.wav").toExternalForm());
  private static final AudioClip bounceBrickSilverGold = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/bounceBrickSilverGold.wav").toExternalForm());
  private static final AudioClip explode = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/explode.wav").toExternalForm());
  private static final AudioClip infinity = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/infinity.wav").toExternalForm());
  private static final AudioClip bubble = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/bubble.wav").toExternalForm());
  private static final AudioClip endLevel = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/endLevel.wav").toExternalForm());
  private static final AudioClip dead = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/dead.wav").toExternalForm());
  private static final AudioClip gameOver = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/gameOver.wav").toExternalForm());
  private static final AudioClip serpentHit = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/serpentHit.wav").toExternalForm());
  private static final AudioClip serpentDrop = new AudioClip(Sound.class.getResource(
      "/com/arkanoid/sound/serpentDrop.wav").toExternalForm());
  private static final Media giantCentipedeMedia = new Media(Sound.class.getResource(
      "/com/arkanoid/sound/giantCentipede.m4a").toExternalForm());
  private static final MediaPlayer giantCentipedeMusic = new MediaPlayer(giantCentipedeMedia);
  private static final Media dohFaceMedia = new Media(Sound.class.getResource
      ("/com/arkanoid/sound/dohFace.m4a").toExternalForm());
  public static final MediaPlayer dohFaceMusic = new MediaPlayer(dohFaceMedia);
  private static final double END_LEVEL_DURATION = 3.5;
  private static volatile boolean endLevelPlaying = false;
  private static PauseTransition endLevelPause;

  public static void playBouncePaddle() {
    bouncePaddle.play();
  }

  public static void playBounceBrick() {
    bounceBrick.play();
  }

  public static void playBounceField() {
    bounceField.play();
  }

  public static void playPowerUpE() {
    powerUpE.play();
  }

  public static void playShoot() {
    Shoot.play();
  }

  public static void playBounceBrickSilverGold() {
    bounceBrickSilverGold.play();
  }

  public static void playExplode() {
    explode.play();
  }

  public static void playInfinity() {
    infinity.setVolume(0.5);
    infinity.play();
  }

  public static void playBubble() {
    bubble.play();
  }

  public static void playDead() {
    dead.play();
  }

  public static void playGameOver() {
    gameOver.play();
  }

  public static void playEndLevel() {
    if (endLevelPause != null) {
      endLevelPause.stop();
      endLevelPause = null;
    }
    endLevelPlaying = true;
    endLevel.play();
    endLevelPause = new PauseTransition(Duration.seconds(END_LEVEL_DURATION));
    endLevelPause.setOnFinished(event -> endLevelPlaying = false);
    endLevelPause.play();
  }

  public static boolean isEndLevelPlaying() {
    return endLevelPlaying;
  }

  public static void playBossHit() {
    serpentHit.play();
  }

  public static void playSerpentDrop() {
    serpentDrop.play();
  }

  public static void playGiantCentipedeMusic() {
    giantCentipedeMusic.setCycleCount(MediaPlayer.INDEFINITE);
    giantCentipedeMusic.play();
  }

  public static void stopGiantCentipedeMusic() {
    giantCentipedeMusic.stop();
  }

  public static void playDohFaceMusic() {
    dohFaceMusic.setCycleCount(MediaPlayer.INDEFINITE);
    dohFaceMusic.play();
  }

  public static void stopDohFaceMusic() {
    dohFaceMusic.stop();
  }
}
