
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {

	private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 16);
    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 36;
    private final int N_BLOCKS = 19;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 6;
    private int PACMAN_SPEED = 3;

    private int N_GHOSTS = 6;
    private int lives, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private Image heart, ghost, blueghost,cherry;
    private Image up, down, left, right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;
    
    private boolean paused = false; 
    private boolean[] ghostVulnerable;
    private long vulnerableStartTime;
    private long speedStartTime;
    private boolean speedTime = false;
    private boolean ghostsVulnerable = false;
    
    private void showMenuScreen(Graphics2D g2d) {
        String start = "Press SPACE to start\n";
        String instructions = "Press I for Instructions";
        String exit = "Press ESC to Exit";
        String pause = "Press P to pause"; // Add pause instruction

        // Set the font and color for the text
        g2d.setColor(Color.YELLOW);
        g2d.setFont(smallFont);
        FontMetrics fm = g2d.getFontMetrics();

        // Calculate widths and heights
        int startWidth = fm.stringWidth(start);
        int instructionsWidth = fm.stringWidth(instructions);
        int exitWidth = fm.stringWidth(exit);
        int pauseWidth = fm.stringWidth(pause); // Calculate width for pause message

        int startHeight = fm.getHeight();

        // Calculate y positions for the strings
        int startY = SCREEN_SIZE / 2;
        int instructionsY = startY + startHeight;
        int pauseY = instructionsY + startHeight; // Position for pause message
        int exitY = pauseY + startHeight; // Position for exit message

        // Set the background color for the text options
        g2d.setColor(Color.DARK_GRAY); // Background color
        g2d.fillRect((SCREEN_SIZE - startWidth) / 2 - 16, startY - startHeight, startWidth + 32, startHeight + 10);
        g2d.fillRect((SCREEN_SIZE - instructionsWidth) / 2 - 10, instructionsY - startHeight, instructionsWidth + 20, startHeight + 10);
        g2d.fillRect((SCREEN_SIZE - pauseWidth) / 2 - 30, pauseY - startHeight, pauseWidth + 60, startHeight + 10); // Background for pause message
        g2d.fillRect((SCREEN_SIZE - exitWidth) / 2 - 28, exitY - startHeight, exitWidth + 56, startHeight + 10);

        // Draw the text on top of the background
        g2d.setColor(Color.YELLOW); // Set text color
        g2d.drawString(start, (SCREEN_SIZE - startWidth) / 2, startY);
        g2d.drawString(instructions, (SCREEN_SIZE - instructionsWidth) / 2, instructionsY);
        g2d.drawString(pause, (SCREEN_SIZE - pauseWidth) / 2, pauseY); // Draw pause message
        g2d.drawString(exit, (SCREEN_SIZE - exitWidth) / 2, exitY);
    }
   

    /*private final short levelData[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21,  0,  0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 18, 18, 16, 16, 16, 16, 16, 24, 24, 24, 24, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
        17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
        21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    */
    private final short levelData[]= { 	
    	    19, 26, 26, 26, 18, 26, 26, 26, 22,  0, 19, 26, 26, 26, 18, 26, 26, 26, 22,
    	    21,  0,  0,  0, 69,  0,  0,  0, 21,  0, 21,  0,  0,  0, 21,  0,  0,  0, 21,
    	    21,  0,  0,  0, 21,  0,  0,  0, 21,  0, 21,  0,  0,  0, 37,  0,  0,  0, 21,
    	    17, 26, 26, 18, 24, 26, 26, 26, 24, 26, 24, 26, 26, 26, 24, 18, 26, 26, 20,
    	    21,  0,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0, 21,
    	    25, 26, 26, 16, 26, 26, 26, 26, 22,  0, 19, 26, 26, 26, 26, 16, 26, 26, 28,
    	     0,  0,  0, 21,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,  0,  0,
    	     0,  0,  0, 21,  0, 19, 26, 26, 24, 18, 24, 26, 26, 22,  0, 21,  0,  0,  0,
    	     0,  0,  0, 21,  0, 21,  0,  0,  0,  5,  0,  0,  0, 21,  0, 21,  0,  0,  0,
    	    27, 26, 26, 16, 26, 20,  0, 11, 10,  8, 10, 14,  0, 17, 26, 16, 26, 42, 30,
    	     0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,
    	     0,  0,  0, 21,  0, 25, 26, 26, 26, 26, 26, 26, 26, 28,  0, 21,  0,  0,  0,
    	     0,  0,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,
    	    19, 26, 26, 16, 26, 26, 26, 26, 22,  0, 19, 26, 26, 26, 26, 16, 26, 26, 22,
    	    21,  0,  0, 21,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,  0, 21,
    	    25, 22,  0, 21,  0, 19, 26, 18, 16, 10, 16, 18, 26, 22,  0, 21,  0, 19, 28,
    	     0, 21,  0, 17, 26, 28,  0, 25, 20,  0, 17, 28,  0, 25, 26, 20,  0, 21,  0,
    	     0, 21,  0, 21,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0, 69,  0,
    	    27, 24, 42, 24, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 24, 26, 24, 30

    	};

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    public Model() {

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
        setBackground(Color.BLACK);
    }

    private void loadImages() {
    	down = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//down.gif").getImage();
    	up = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//up.gif").getImage();
    	left = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//left.gif").getImage();
    	right = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//right.gif").getImage();
        ghost = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//ghost.gif").getImage();
        blueghost =  new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//blueghost.gif").getImage();
        cherry =  new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//CherryShadow.png").getImage();
        heart = new ImageIcon("C://Users//Robert//Downloads//pacman-main//pacman-main//images//heart.png").getImage();

    }
       private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        ghostVulnerable = new boolean[MAX_GHOSTS];
        
        timer = new Timer(40, this);
        timer.start();
    }

       private void playGame(Graphics2D g2d) {
    	    checkAndResetSpeed(); // Check and reset speed here

    	    if (dying) {
    	        death();
    	    } else {
    	        movePacman();
    	        drawPacman(g2d);
    	        moveGhosts(g2d);
    	        checkMaze();
    	    }
    	}

    	private void checkAndResetSpeed() {
    	    if (speedTime && (System.currentTimeMillis() - speedStartTime) >= 5000) {
    	        PACMAN_SPEED = 3; 
    	        speedTime = false; 
    	    }
    	}

    private void showIntroScreen(Graphics2D g2d) {
	    	String start = "Press SPACE to start";
	        g2d.setColor(Color.yellow);
	        g2d.setBackground(Color.WHITE);
	        FontMetrics fm = g2d.getFontMetrics();
	        int stringWidth = fm.stringWidth(start);
	        int stringHeight = fm.getHeight();
	        int x = (SCREEN_SIZE - stringWidth) / 2;
	        int y = (SCREEN_SIZE + stringHeight) / 2;
	        g2d.drawString(start, x, y);
    }
        

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 16) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    private void death() {

    	lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {
                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1, ghostVulnerable[i]);

            
            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                if (ghostVulnerable[i]) { 
  
                    score += 300; 
                    	
                    resetGhost(i); 
                } else {
                    dying = true;
                }
            }
        }
        if (ghostsVulnerable && (System.currentTimeMillis() - vulnerableStartTime) >= 8000) {
            for (int i = 0; i < N_GHOSTS; i++) {
                ghostVulnerable[i] = false; 
            }
            ghostsVulnerable = false; 
        }
    }
      

    private void drawGhost(Graphics2D g2d, int x, int y, boolean isVulnerable) {
        if (isVulnerable) {
            g2d.drawImage(blueghost, x + 5, y + 5, this);
        } else {
            g2d.drawImage(ghost, x + 5, y + 5, this); 
        }
    }
    
    private void drawFruit(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(cherry, x + 5, y + 5, this);
    }
    
    private void movePacman() {
        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score += 10;
                
            } else if ((ch & 32) != 0) {
                score += 100;
                currentSpeed = 1;
                screenData[pos] = (short) (ch & 31);
                for (int i = 0; i < N_GHOSTS; i++) {
                	currentSpeed = 1;
                	
                    ghostVulnerable[i] = true;
                }
                ghostsVulnerable = true;
                vulnerableStartTime = System.currentTimeMillis();
                
            } else if ((ch & 64) != 0) {
                screenData[pos] = (short) (ch & 63);
                score += 50;
                PACMAN_SPEED = 6;
                speedStartTime = System.currentTimeMillis();
                speedTime = true;
            }


            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }


            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }

        pacman_x += PACMAN_SPEED * pacmand_x;
        pacman_y += PACMAN_SPEED * pacmand_y;

        if (pacman_x < 0) pacman_x = 0;
        if (pacman_x >= SCREEN_SIZE) pacman_x = SCREEN_SIZE - 1;
        if (pacman_y < 0) pacman_y = 0;
        if (pacman_y >= SCREEN_SIZE) pacman_y = SCREEN_SIZE - 1;
    }
    
    private void resetGhost(int ghostIndex) {
        ghost_x[ghostIndex] = (N_BLOCKS / 2) * BLOCK_SIZE; 
        ghost_y[ghostIndex] = (N_BLOCKS / 2) * BLOCK_SIZE; 
        ghostVulnerable[ghostIndex] = false; 
    }

    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 7, pacman_y + 6, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 7, pacman_y + 6, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 7, pacman_y + 6, this);
        } else {
        	g2d.drawImage(down, pacman_x + 7, pacman_y + 6, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i=0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));
                
                if ((levelData[i] == 0)) {
                	g2d.setColor(new Color(0,0,0));
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                 }

                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 3);
                }

                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 3  , y);
                }

                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 3, y, x + BLOCK_SIZE - 3,
                            y + BLOCK_SIZE - 3);
                }

                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 3, x + BLOCK_SIZE - 3,
                            y + BLOCK_SIZE - 3);
                }
                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 13, y + 13, 6, 6);
               }
                
                if((screenData[i] & 32) !=0) {
                	g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 9, y + 9, 16, 16);
                    
                }
                if((screenData[i] & 64) !=0) {
                	drawFruit(g2d, x + 1 , y + 2); 
                    
                }
                
                
                i++;
            }
        }
    }

    private void initGame() {

    	lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 1;
        currentSpeed = 2;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }

    private void continueLevel() {

    	int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {

        	ghost_y[i] = (N_BLOCKS / 2) * BLOCK_SIZE;
            ghost_x[i] = (N_BLOCKS / 2) * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[1];
        }

        pacman_x = (N_BLOCKS / 2) * BLOCK_SIZE;  //start position
        pacman_y = 15 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (paused) {
            showPausedScreen(g2d); // Show paused screen if paused
        } else if (inGame) {
            playGame(g2d);
        } else if (!inGame) {
            showMenuScreen(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void showPausedScreen(Graphics2D g2d) {
        String pausedMessage = "Game Paused. Press P to resume.";
        g2d.setColor(Color.YELLOW);
        g2d.setFont(smallFont);
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(pausedMessage);
        g2d.drawString(pausedMessage, (SCREEN_SIZE - messageWidth) / 2, SCREEN_SIZE / 2);
    }

    //controls
    class TAdapter extends KeyAdapter {

    	@Override
    	public void keyPressed(KeyEvent e) {
    	    int key = e.getKeyCode();

    	    if (inGame) {
    	        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
    	            req_dx = -1;
    	            req_dy = 0;
    	        } else if (key == KeyEvent.VK_RIGHT|| key == KeyEvent.VK_D) {
    	            req_dx = 1;
    	            req_dy = 0;
    	        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
    	            req_dx = 0;
    	            req_dy = -1;
    	        } else if (key == KeyEvent.VK_DOWN|| key == KeyEvent.VK_S) {
    	            req_dx = 0;
    	            req_dy = 1;
    	        } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
    	            inGame = false;
    	        } else if (key == KeyEvent.VK_P) { 
    	            paused = !paused;
    	            if (paused) {
    	                timer.stop(); 
    	            } else {
    	                timer.start(); 
    	            }
    	        }
    	    } else {
    	        if (key == KeyEvent.VK_SPACE) {
    	            inGame = true;
    	            initGame();
    	        } else if (key == KeyEvent.VK_I) {
    	            showInstructions();
    	        } else if (key == KeyEvent.VK_ESCAPE) {
    	            System.exit(0); // Exit the game
    	        }
    	    }
    	}
        
    }
    
    private void showInstructions () {
    	JOptionPane.showMessageDialog(this, "Instructions:\nUse arrow keys to move Pacman."
    			+ "							\nEat all the dots to win!\nAvoid the ghosts!", 
    										"Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();                
    }

    		
	}