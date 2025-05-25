import java.awt.*;

public class SuperJumpPowerUp extends PowerUp {

    public SuperJumpPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        if (!isCollected()) {
            g.setColor(Color.RED); // 💠 A new look for super jump
            g.fillOval(getX(), getY(), 25, 25); // same size as base orb
        }
    }
}
