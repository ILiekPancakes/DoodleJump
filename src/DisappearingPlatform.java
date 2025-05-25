import java.awt.*;
import java.awt.Image;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DisappearingPlatform extends Platform {
private Image image;

    public DisappearingPlatform(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
    if (image == null) {
        try {
            image = ImageIO.read(new File("bpickle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    if (image != null) {
        g.drawImage(image, getX(), getY(), 60, 10, null);
    }
  }
}
