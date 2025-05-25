import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Image;

public class DoodleJump extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Impostor impostor;
    private ArrayList<Platform> platforms;
    private boolean gameOver;
    private JButton retryButton;
    private boolean gameStarted = false;
    private ArrayList<PowerUp> powerUps;
    private int powerTimer = 0;
    private int level = 1;
    private int previousX;
    private int previousY;
    private Image bg1, bg2, bg3, bg4;


    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    public DoodleJump() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        addKeyListener(this);
        setFocusable(true);

        impostor = new Impostor(WIDTH / 2 - 20, HEIGHT - 100);
        platforms = new ArrayList<>();
        powerUps = new ArrayList<>();
        generatePlatforms();
        
        

        timer = new Timer(10, this);
        timer.start();
        
        retryButton = new JButton("Retry");
        retryButton.setFocusable(false); // So arrow keys still work
        retryButton.setVisible(false);   // Hide until game over
        retryButton.addActionListener(e -> restartGame());
        this.add(retryButton);
        
        try {
    bg1 = ImageIO.read(new File("level1.jpg"));
    bg2 = ImageIO.read(new File("level2.jpg"));
    bg3 = ImageIO.read(new File("level3.jpg"));
    bg4 = ImageIO.read(new File("level4.jpg"));
} catch (IOException e) {
    e.printStackTrace();
}
    }

    public void actionPerformed(ActionEvent e) {
    if (!gameStarted || gameOver) {
        repaint(); // allow visuals to update even if paused
        return;
}

      if (powerTimer > 0) {
    powerTimer--;
    if (powerTimer == 0) {
        impostor.setMoveSpeed(5);
        impostor.setGravity(1);         // reset to normal
        impostor.setJumpStrength(15);
        }
    }

    impostor.update();

    if (impostor.getY() < 300) {
        int dy = 300 - impostor.getY();
        impostor.shiftY(dy);
        for (Platform p : platforms) {
            p.moveDown(dy);
        }
        
        for (PowerUp pu : powerUps) {
        pu.moveDown(dy);
    }
        
        generateMorePlatforms();
    }
    
    int score = -impostor.getTotalClimb(); // climbing = negative Y
         if (score >= 5000) {
               level = 4;
         } else if (score >= 3000) {
               level = 3;
         } else if (score >= 1000) {
               level = 2;
         } else {
               level = 1;
         }   
         
    checkCollisions();
    repaint();  
         
}


    public void generatePlatforms() {
    platforms.clear();
    powerUps.clear();

    // Starting platform
    int startX = (WIDTH / 2) - 30;
    int startY = HEIGHT - 50;
    platforms.add(new Platform(startX, startY));

    // Regular platforms
    int previousX = startX;
    int previousY = startY;

    for (int i = 0; i < 10; i++) {
    int yOffset = (int) (Math.random() * 50 + 40);
    int newY = previousY - yOffset;
    int maxXOffset = WIDTH / 4;
    int xOffset = (int) (Math.random() * (maxXOffset * 2)) - maxXOffset;
    int newX = Math.max(0, Math.min(WIDTH - 60, previousX + xOffset));

    platforms.add(new Platform(newX, newY));
    
      
        if (Math.random() < 0.1) {
            if (Math.random() < 0.5) {
                powerUps.add(new PowerUp(newX + 20, newY - 30));
            } else {
                powerUps.add(new SuperJumpPowerUp(newX + 20, newY - 30));
            }
        }
        
    previousX = newX;
    previousY = newY;
    
    }
}




    public void checkCollisions() {
        for (Platform p : platforms) {
            if (impostor.getBounds().intersects(p.getBounds())) {
    if (impostor.getVelY() > 0) {
        impostor.jump();

        // 💥 If it's a disappearing platform, remove it
        if (p instanceof DisappearingPlatform) {
            platforms.remove(p);
        }
        
        for (PowerUp pu : powerUps) {
    if (!pu.isCollected() && impostor.getBounds().intersects(pu.getBounds())) {
        pu.collect();

        if (pu instanceof SuperJumpPowerUp) {
            impostor.setJumpStrength(30); // 💥 super jump!
            powerTimer = 500; // shared timer for now
        } else {
            impostor.setGravity(0); // 🌕 moon gravity
            impostor.setMoveSpeed(2); // slow left/right
            powerTimer = 200; // shared timer for now
        }

        
    }
}

        break;
    }
}
        }

        if (impostor.getY() > HEIGHT) {
            gameOver = true;
        }
    }

    public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    Image bg = switch (level) {
    case 1 -> bg1;
    case 2 -> bg2;
    case 3 -> bg3;
    default -> bg4;
};

if (bg != null) {
    g.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
}

    // Always draw impostor and platforms
    impostor.draw(g);
    for (Platform p : platforms) {
        p.draw(g);
    }
    
    for (PowerUp pu : powerUps) {
      pu.draw(g);
    }

    // Draw score only during play
    if (gameStarted && !gameOver) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + (-impostor.getTotalClimb()), 10, 25);
        g.drawString("Level: " + level, 10, 45);
    }

    // Draw game over
    if (gameOver) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("GAME OVER", 60, HEIGHT / 2);
        retryButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 50, 100, 30);
        retryButton.setVisible(true);
    } else {
        retryButton.setVisible(false);
    }

    // ✅ Draw "Press Any Key" message if not started
    if (!gameStarted) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Press Any Key to Start", 60, HEIGHT / 2);
    }
    
}


    public void keyPressed(KeyEvent e) {
    if (!gameStarted) {
        gameStarted = true;
        repaint();
        return;
    }

    if (e.getKeyCode() == KeyEvent.VK_LEFT) impostor.setLeft(true);
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) impostor.setRight(true);
}



    public void keyReleased(KeyEvent e) {
    if (!gameStarted) return;

    if (e.getKeyCode() == KeyEvent.VK_LEFT) impostor.setLeft(false);
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) impostor.setRight(false);
}


    public void keyTyped(KeyEvent e) {}
    
    public void generateMorePlatforms() {
    // Remove platforms that are now below the screen
    platforms.removeIf(p -> p.getY() > HEIGHT);

    // Find highest platform Y
    int highestY = HEIGHT;
    for (Platform p : platforms) {
        if (p.getY() < highestY) {
            highestY = p.getY();
        }
    }

    // Add new platforms above the highest one if needed
    while (highestY > 0) {
    int yOffset = (int) (Math.random() * 50 + 40);
    int newY = highestY - yOffset;

    int maxXOffset = WIDTH / 4;
    int lastX = platforms.get(0).getX(); // base from one of the previous platforms
    int xOffset = (int) (Math.random() * (maxXOffset * 2)) - maxXOffset;
    int newX = Math.max(0, Math.min(WIDTH - 60, lastX + xOffset));

    // 🔁 LEVEL-BASED PLATFORM LOGIC
    if (level == 1) {
        platforms.add(new Platform(newX, newY));
    } else if (level == 2) {
        if (Math.random() < 0.2) {
            platforms.add(new DisappearingPlatform(newX, newY));
        } else {
            platforms.add(new Platform(newX, newY));
        }
    } else if (level == 3) {
        if (Math.random() < 0.3) {
            platforms.add(new DisappearingPlatform(newX, newY));
        } else {
            platforms.add(new Platform(newX, newY));
        } 
    } else if (level >= 4) {
        if (Math.random() < 0.6) {
            platforms.add(new DisappearingPlatform(newX, newY));
        } else {
            platforms.add(new Platform(newX, newY));
        }
        
        if (Math.random() < 0.1) {
            if (Math.random() < 0.5) {
                powerUps.add(new PowerUp(newX + 20, newY - 30));
            } else {
                powerUps.add(new SuperJumpPowerUp(newX + 20, newY - 30));
            }
        }

        
    }

    highestY = newY;
}

   }
   
   public void restartGame() {
    // Reset state
    platforms.clear();
    powerUps.clear();
    gameOver = false;
    gameStarted = false;
    retryButton.setVisible(false);

    // Reset impostor
    impostor = new Impostor(WIDTH / 2 - 20, HEIGHT - 100);

    // ⬇️ Reset platform reference points BEFORE calling generatePlatforms
    previousX = WIDTH / 2 - 30;
    previousY = HEIGHT - 50;

    // Add guaranteed base platform directly under player
    platforms.add(new Platform(previousX, previousY));

    // Now generate more platforms
    generatePlatforms();

    repaint();
}
   
   
}
