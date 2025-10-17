package com.arkanoid.enemies;

import com.arkanoid.core.Ball;
import com.arkanoid.core.BallManager;

import java.util.ArrayList;

import javafx.scene.layout.AnchorPane;
import com.arkanoid.Controller;
import javafx.scene.shape.Rectangle;


public class GiantCentipedeBoss extends Enemies {

    public static ArrayList<GiantCentipedeSegment> GiantCentipede = new ArrayList<>();
    int hp = 20;
    int hitCooldown = 0;
    int dropCooldown = 80;
    int dropOrder = 0;
    int stateCooldown = 200;
    int state = 0;

    public GiantCentipedeBoss(double x, double y, double r, AnchorPane scene) {
        super(x, y, r);
        double speed = 0.058;

        GiantCentipedeSegment head = new GiantCentipedeSegment(x, y, r, true, speed);
        GiantCentipede.add(head);

        for (int i = 0; i < 7; i++) {
            x -= 2 * r;
            speed -= 0.002;
            GiantCentipedeSegment seg = new GiantCentipedeSegment(x, y, r, false, speed);
            GiantCentipede.add(seg);
        }

        for (int i = 7; i >= 0; i--) {
            GiantCentipedeSegment seg = GiantCentipede.get(i);
            scene.getChildren().add(seg.getImageView());
        }
    }

    public void clear(AnchorPane scene) {
        for (GiantCentipedeSegment seg : GiantCentipede)
            scene.getChildren().remove(seg.getImageView());
        EnemiesManager.setGameOver(true);
    }

    @Override
    public boolean update(double DeltaTime, AnchorPane scene) {
        double fX = 0;
        double fY = 0;
        boolean checkCollision = false;

        for (GiantCentipedeSegment seg : GiantCentipede) {
            seg.move(fX, fY, DeltaTime, state);
            fX = seg.getCircle().getCenterX();
            fY = seg.getCircle().getCenterY();
            //System.exit(0);
            if (hitCooldown <= 0) {
                seg.changeState(0, 0);
                for (Ball ball : BallManager.getBalls())
                    if (ball.getShape().getBoundsInParent().intersects(seg.getShape().getBoundsInParent())) {
                        checkCollision = true;
                        BallManager.checkCollisionScene(this.getRectangle());
                        ball.update(scene);
                    }
            } else {
                seg.changeState(1, 0);
            }
        }

        if (checkCollision) {
            hp--;
            hitCooldown = 30;

            if (hp == 0) {
                clear(scene);
                return true;
            }
        }

        if (state == 0 && hitCooldown <= 0) {
            GiantCentipede.get(dropOrder).changeState(0
                    , 2 - (dropCooldown / 33));
        }

        hitCooldown--;
        dropCooldown--;
        stateCooldown--;

        switch (state) {
            case 0:
                //System.exit(0);

                if (dropCooldown == 0) {
                    dropCooldown = 80;
                    double X = GiantCentipede.get(dropOrder).getCircle().getCenterX();
                    double Y = GiantCentipede.get(dropOrder).getCircle().getCenterY();
                    Debris nDeb = new Debris(X, Y, 2);
                    //nDeb.getCircle().setFill(Color.GREEN);
                    scene.getChildren().add(nDeb.getImageView());
                    EnemiesManager.enemies.add(nDeb);
                    dropOrder++;
                    dropOrder %= GiantCentipede.size();
                }

                if (stateCooldown <= 0
                        && GiantCentipede.get(0).getMid()) {
                    state = 1;
                    stateCooldown = 200;
                }
                break;
            case 1:
                stateCooldown--;
                if (stateCooldown <= 0) {
                    state = 2;
                }
                break;
            case 2:

                for (GiantCentipedeSegment seg : GiantCentipede)
                    if (seg.getCircle().getBoundsInParent().intersects(
                            Controller.paddle.getRectangle().getBoundsInParent())) {
                        clear(scene);
                        return true;
                    }

                Rectangle rect = Controller.field.getRectangle();
                GiantCentipedeSegment last = GiantCentipede.get(GiantCentipede.size() - 1);

                if (last.getCircle().getCenterY() - last.getCircle().getRadius()
                        > rect.getY() + rect.getHeight()) {
                    state = 3;
                    stateCooldown = 200;
                    for (GiantCentipedeSegment seg : GiantCentipede) {
                        seg.getCircle().setCenterX(rect.getX()
                                + seg.getCircle().getRadius());
                        seg.getCircle().setCenterY(rect.getY()
                                - seg.getCircle().getRadius());

                    }
                }
                break;
            case 3:
                stateCooldown--;
                if (stateCooldown <= 0) {
                    state = 4;
                    GiantCentipede.get(0).setChanged(false);
                }
                break;
            case 4:
                if (GiantCentipede.get(0).getMid()) {
                    GiantCentipede.get(0).setChanged(false);
                    state = 0;
                    stateCooldown = 200;
                    dropCooldown = 80;
                }
                break;
            case 5:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }

        return false;
    }


}
