package com.arkanoid.enemies;

import com.arkanoid.brick.Brick;
import com.arkanoid.brick.BrickManager;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class TriangleC extends Enemies {
    private int MoveCooldown;
    double Amplitude = 25;
    double SwingSpeed = 1.5;
    double FallSpeed = 10;
    double BaseX = 0;
    private final Image[][] images = new Image[2][12];
    private int state = 0;
    private int imageState;
    private int imageCooldown;

    public TriangleC (double x, double y, double w, double h) {
        super(x, y, w, h);
        BaseX = x;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < (i == 1 ? 12 : 8); j++)
                images[i][j] = new Image(getClass().getResource(
                    "/com/arkanoid/Enemy/TriangleC/TriangleC" + i + j + ".png").toExternalForm());
        state = 0;
        imageState = 0;
        imageCooldown = 20;
    }
    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        switch(state) {
            case 0:
                boolean bottomBorder = false;
                boolean leftBorder = false;
                boolean rightBorder = false;
                for(Brick brick : BrickManager.getBricks())
                    if (brick.getShape().getBoundsInParent().intersects(this.getShape().getBoundsInParent())) {
                        if (this.getRectangle().getY() + this.getRectangle().getHeight() >= brick.getRectangle().getY()) {
                            bottomBorder = true;
                        }
                        if (this.getRectangle().getX() <= brick.getRectangle().getX() +  brick.getRectangle().getWidth()
                                && this.getRectangle().getX() >= brick.getRectangle().getX()) {
                            leftBorder = true;
                        }
                        if (this.getRectangle().getX() + this.getRectangle().getWidth() >= brick.getRectangle().getX()
                                && this.getRectangle().getX() <= brick.getRectangle().getX()) {
                            rightBorder = true;
                        }
                    }


                totalTime =  totalTime + DeltaTime;
                if(!bottomBorder) {
                    double oldY = this.getRectangle().getY();
                    this.getRectangle().setY(this.getRectangle().getY() + FallSpeed * DeltaTime);
                    if (this.checkCollisionEnemy() || this.checkCollisionScene()) {
                        this.getRectangle().setY(oldY);
                    }
                }

                double ChangeX = Amplitude * Math.sin(SwingSpeed * totalTime);
                if((leftBorder && ChangeX < 0) || (rightBorder && ChangeX > 0)) {
                    Amplitude = ChangeX = 0;
                    BaseX = this.getRectangle().getX();
                }
                double oldX = this.getRectangle().getX();
                this.getRectangle().setX(BaseX + ChangeX);
                if(this.checkCollisionEnemy() || this.checkCollisionScene()) {
                    this.getRectangle().setX(oldX);
                    totalTime = totalTime - DeltaTime;
                }

                if (this.checkCollisionBall()) {
                    state = 1;
                    imageCooldown = 20;
                    imageState = 0;
                    MoveCooldown = imageCooldown * 12;
                }
                else {
                    imageCooldown--;
                    if (imageCooldown == 0) {
                        imageState++;
                        imageState %= 8;
                        imageCooldown = 20;
                    }
                }
            break;
            case 1:
                imageCooldown--;
                MoveCooldown--;
                if (MoveCooldown == 0) return true;
                if (imageCooldown == 0) {
                    imageState++;
                    imageCooldown = 20;
                }
            break;
        }

        imageView.setImage(images[state][imageState]);
        imageView.setX(this.getRectangle().getX() + this.getRectangle().getWidth() / 2 - images[state][imageState].getWidth() / 2);
        imageView.setY(this.getRectangle().getY() + this.getRectangle().getHeight() / 2 - images[state][imageState].getHeight() / 2);
        return false;
    }
}