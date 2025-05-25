import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Image;

public class Platform {
    private int x, y;
    private int width = 60, height = 10;
    protected Image image;

    public Platform(int x, int y) {
        this.x = x;
        this.y = y;
        try {
    image = ImageIO.read(new File("pickle.png")); 
} catch (IOException e) {
    e.printStackTrace();
}
    }

    public void draw(Graphics g) {
      g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void moveDown(int dy) {
    y += dy;
    }
    
   public int getX() {
       return x;
   }

   public int getY() {
       return y;
   }
}
   