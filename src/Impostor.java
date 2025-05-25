import java.awt.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Impostor {
    private int x, y;
    private int width = 60, height = 60;
    private int velX = 0, velY = 0;
    private boolean left, right;
    private int totalClimb = 0;
    private int moveSpeed = 5;
    private int gravity = 1;
    private int jumpStrength = 15;
    private Image image;
    

    public Impostor(int x, int y) {
        this.x = x;
        this.y = y;
        try {
        image = ImageIO.read(new File("amogus.png"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void update() {
    if (left) velX = -moveSpeed;
    else if (right) velX = moveSpeed;
    else velX = 0;

    velY += gravity; // gravity
   
    x += velX;
    y += velY;

    if (x > DoodleJump.WIDTH) {
        x = -width;
    } else if (x + width < 0) {
        x = DoodleJump.WIDTH;
    }
}


    public void jump() {
        velY = -jumpStrength;
        playJumpSound();
    }

    public void draw(Graphics g) {
    g.drawImage(image, x, y, width, height, null);
}

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() { return y; }
    public int getVelY() { return velY; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }

    public void setY(int newY) {
        this.y = newY;
    }

    public void shiftY(int dy) {
        y += dy;
        totalClimb -= dy;
    }
    
    public int getTotalClimb() {
    return totalClimb;
    }
    
    public void setMoveSpeed(int speed) {
    this.moveSpeed = speed;
    }
    
    public void setGravity(int g) {
    this.gravity = g;
    }

    public void setJumpStrength(int js) {
    this.jumpStrength = js;
    }
    
    private void playJumpSound() {
    try {
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File("jump.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
}
