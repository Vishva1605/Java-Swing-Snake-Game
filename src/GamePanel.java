import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;        // Size of one grid cell
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 80;            // Game speed in ms (lower = faster)

    final int[] x = new int[GAME_UNITS];    // X coordinates of snake segments
    final int[] y = new int[GAME_UNITS];    // Y coordinates of snake segments
    int bodyParts = 6;
    int applesEaten = 0;
    int highScore = 0;
    int appleX;
    int appleY;
    char direction = 'R';                   // Direction buffer for next tick: 'U', 'D', 'L', 'R'
    char currentDirection = 'R';            // Currently active direction during the last move
    boolean running = false;
    boolean paused = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(20, 22, 30));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        currentDirection = 'R';
        paused = false;

        // Position initial snake horizontally in the middle of the screen
        int startX = (SCREEN_WIDTH / (2 * UNIT_SIZE)) * UNIT_SIZE;
        int startY = (SCREEN_HEIGHT / (2 * UNIT_SIZE)) * UNIT_SIZE;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - (i * UNIT_SIZE);
            y[i] = startY;
        }

        running = true;
        newApple();

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(DELAY, this);
        timer.start();
        repaint();
    }

    public void restartGame() {
        startGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw subtle background grid
        g2d.setColor(new Color(30, 33, 44));
        for (int i = 0; i <= SCREEN_WIDTH / UNIT_SIZE; i++) {
            g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        for (int i = 0; i <= SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g2d.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        if (running) {
            // Draw apple
            drawApple(g2d);

            // Draw snake
            drawSnake(g2d);

            // Draw HUD (Score & High Score)
            drawHUD(g2d);

            // Draw pause screen overlay if paused
            if (paused) {
                drawPauseOverlay(g2d);
            }
        } else {
            // Game over screen
            gameOver(g2d);
        }
    }

    private void drawApple(Graphics2D g2d) {
        // Apple stem
        g2d.setColor(new Color(121, 85, 72));
        g2d.fillRect(appleX + UNIT_SIZE / 2 - 1, appleY + 1, 2, 4);

        // Apple leaf
        g2d.setColor(new Color(76, 175, 80));
        g2d.fillOval(appleX + UNIT_SIZE / 2 + 1, appleY + 1, 5, 3);

        // Apple body
        g2d.setColor(new Color(239, 83, 80));
        g2d.fillOval(appleX + 2, appleY + 4, UNIT_SIZE - 4, UNIT_SIZE - 5);

        // Apple highlight reflection
        g2d.setColor(new Color(255, 205, 210, 180));
        g2d.fillOval(appleX + 6, appleY + 7, 4, 4);
    }

    private void drawSnake(Graphics2D g2d) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                // Snake head
                g2d.setColor(new Color(67, 160, 71));
                g2d.fillRoundRect(x[i] + 1, y[i] + 1, UNIT_SIZE - 2, UNIT_SIZE - 2, 8, 8);

                // Draw snake eyes based on direction
                drawEyes(g2d, x[i], y[i], currentDirection);
            } else {
                // Alternating gradient shades for body
                if (i % 2 == 0) {
                    g2d.setColor(new Color(76, 175, 80));
                } else {
                    g2d.setColor(new Color(102, 187, 106));
                }
                g2d.fillRoundRect(x[i] + 1, y[i] + 1, UNIT_SIZE - 2, UNIT_SIZE - 2, 6, 6);
            }
        }
    }

    private void drawEyes(Graphics2D g2d, int headX, int headY, char dir) {
        int eye1X = headX, eye1Y = headY, eye2X = headX, eye2Y = headY;
        int pupil1X = headX, pupil1Y = headY, pupil2X = headX, pupil2Y = headY;
        int eyeSize = 5;
        int pupilSize = 3;

        switch (dir) {
            case 'R':
                eye1X = headX + 15; eye1Y = headY + 4;
                eye2X = headX + 15; eye2Y = headY + 14;
                pupil1X = headX + 17; pupil1Y = headY + 5;
                pupil2X = headX + 17; pupil2Y = headY + 15;
                break;
            case 'L':
                eye1X = headX + 4; eye1Y = headY + 4;
                eye2X = headX + 4; eye2Y = headY + 14;
                pupil1X = headX + 4; pupil1Y = headY + 5;
                pupil2X = headX + 4; pupil2Y = headY + 15;
                break;
            case 'U':
                eye1X = headX + 4; eye1Y = headY + 4;
                eye2X = headX + 14; eye2Y = headY + 4;
                pupil1X = headX + 5; pupil1Y = headY + 4;
                pupil2X = headX + 15; pupil2Y = headY + 4;
                break;
            case 'D':
                eye1X = headX + 4; eye1Y = headY + 15;
                eye2X = headX + 14; eye2Y = headY + 15;
                pupil1X = headX + 5; pupil1Y = headY + 17;
                pupil2X = headX + 15; pupil2Y = headY + 17;
                break;
        }

        // Eye whites
        g2d.setColor(Color.WHITE);
        g2d.fillOval(eye1X, eye1Y, eyeSize, eyeSize);
        g2d.fillOval(eye2X, eye2Y, eyeSize, eyeSize);

        // Pupils
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillOval(pupil1X, pupil1Y, pupilSize, pupilSize);
        g2d.fillOval(pupil2X, pupil2Y, pupilSize, pupilSize);
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Score
        g2d.setColor(Color.WHITE);
        String scoreText = "Score: " + applesEaten;
        g2d.drawString(scoreText, 20, 30);

        // High Score
        g2d.setColor(new Color(255, 215, 0));
        String highScoreText = "Best: " + highScore;
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.drawString(highScoreText, SCREEN_WIDTH - metrics.stringWidth(highScoreText) - 20, 30);
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 50));
        FontMetrics fm1 = g2d.getFontMetrics();
        String pauseText = "PAUSED";
        g2d.drawString(pauseText, (SCREEN_WIDTH - fm1.stringWidth(pauseText)) / 2, SCREEN_HEIGHT / 2 - 20);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2d.setColor(new Color(200, 200, 200));
        FontMetrics fm2 = g2d.getFontMetrics();
        String resumeText = "Press 'P' or 'SPACE' to Resume";
        g2d.drawString(resumeText, (SCREEN_WIDTH - fm2.stringWidth(resumeText)) / 2, SCREEN_HEIGHT / 2 + 30);
    }

    public void newApple() {
        if (bodyParts >= GAME_UNITS) {
            running = false;
            if (timer != null) timer.stop();
            return;
        }

        boolean onSnake;
        do {
            onSnake = false;
            appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

            // Ensure apple does not spawn on the snake
            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == appleX && y[i] == appleY) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    public void move() {
        // Shift body parts
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Update direction
        currentDirection = direction;

        // Move head
        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            if (applesEaten > highScore) {
                highScore = applesEaten;
            }
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        // Check if head touches screen borders
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            if (timer != null) {
                timer.stop();
            }
        }
    }

    public void gameOver(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Dim background
        g2d.setColor(new Color(0, 0, 0, 190));
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // "Game Over" Title
        g2d.setColor(new Color(239, 83, 80));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 58));
        FontMetrics titleMetrics = g2d.getFontMetrics();
        String title = "GAME OVER";
        g2d.drawString(title, (SCREEN_WIDTH - titleMetrics.stringWidth(title)) / 2, SCREEN_HEIGHT / 2 - 80);

        // Current Score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 26));
        FontMetrics scoreMetrics = g2d.getFontMetrics();
        String scoreMsg = "Score: " + applesEaten;
        g2d.drawString(scoreMsg, (SCREEN_WIDTH - scoreMetrics.stringWidth(scoreMsg)) / 2, SCREEN_HEIGHT / 2 - 20);

        // High Score
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics highMetrics = g2d.getFontMetrics();
        String highMsg = "Best Score: " + highScore;
        g2d.drawString(highMsg, (SCREEN_WIDTH - highMetrics.stringWidth(highMsg)) / 2, SCREEN_HEIGHT / 2 + 25);

        // Restart Instructions
        g2d.setColor(new Color(176, 190, 197));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 18));
        FontMetrics restartMetrics = g2d.getFontMetrics();
        String restartMsg = "Press 'R' or 'SPACE' to Play Again";
        g2d.drawString(restartMsg, (SCREEN_WIDTH - restartMetrics.stringWidth(restartMsg)) / 2, SCREEN_HEIGHT / 2 + 90);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // Controls during gameplay
            if (running) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        if (currentDirection != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        if (currentDirection != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        if (currentDirection != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        if (currentDirection != 'U') {
                            direction = 'D';
                        }
                        break;
                    case KeyEvent.VK_P:
                        paused = !paused;
                        repaint();
                        break;
                    case KeyEvent.VK_SPACE:
                        paused = !paused;
                        repaint();
                        break;
                }
            } else {
                // Controls on Game Over screen
                if (key == KeyEvent.VK_R || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    restartGame();
                }
            }
        }
    }
}
