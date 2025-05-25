import java.awt.*;

public class PowerUp {
    private int x, y;
    private int size = 25;
    private boolean collected = false;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (!collected) {
            g.setColor(Color.ORANGE);
            g.fillOval(x, y, size, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public int getX() {
    return x;
    }

    public int getY() {
    return y;
    }

    public void moveDown(int dy) {
        y += dy;
    }
}
