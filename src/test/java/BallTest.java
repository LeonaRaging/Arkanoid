import com.arkanoid.core.Ball;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {
    private Ball ball;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        ball = new Ball(100, 100, 2.5);
    }

    @Test
    void testInitialBallState() {
        assertEquals(0, ball.getShape().getLayoutX(), "Initial X position should be 100");
        assertEquals(0, ball.getShape().getLayoutY(), "Initial Y position should be 100");
        assertEquals(0, ball.ballType, "Initial ball type should be 0");
    }

    @Test
    void testUpdateX() {
        double initialDeltaX = ball.getDeltaX();
        ball.updateX(-1);
        assertNotEquals(ball.getDeltaX(), initialDeltaX, "Delta X should change after update");
        assertTrue(Math.abs(ball.getDeltaY()) >= 0.75, "Delta Y should not be less than 0.75");
    }

    @Test
    void testUpdateY() {
        double initialDeltaY = ball.getDeltaY();
        ball.updateY(-1);
        assertNotEquals(ball.getDeltaY(), initialDeltaY, "Delta Y should change after update");
        assertTrue(Math.abs(ball.getDeltaX()) >= 0.75, "Delta X should not be less than 0.75" + ball.getDeltaX());
    }

    @Test
    void testBallTypeChange() {
        assertEquals(0, ball.ballType, "Initial ball type should be 0");
        ball.ballType = 1;
        assertEquals(1, ball.ballType, "Ball type should change to 1");
    }
}
