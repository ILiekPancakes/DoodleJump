import java.awt.*;

public class SuperJumpPowerUp extends PowerUp {

    public SuperJumpPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        if (!isCollected()) {
            g.setColor(Color.RED); 
            g.fillOval(getX(), getY(), 25, 25); 
        }
    }
}
