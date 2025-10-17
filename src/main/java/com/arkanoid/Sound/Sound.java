package com.arkanoid.sound;

import javafx.scene.media.AudioClip;

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
    infinity.play();
  }

  public static void playBubble() {
    bubble.play();
  }
}
