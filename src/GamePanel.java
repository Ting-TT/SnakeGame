import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int HORIZONTAL_BLOCKS = SCREEN_WIDTH / UNIT_SIZE; // HORIZONTAL_BLOCKS = 20
    static final int VERTICAL_BLOCKS = SCREEN_HEIGHT/ UNIT_SIZE;
    static final int TOTAL_NUMBER_OF_BLOCKS = HORIZONTAL_BLOCKS * VERTICAL_BLOCKS;
    static final int DELAY = 100; // the delay for the timer, controls how fast the game/move is

    // all X and Y coordinates denote the left upper corner of the block
    // e.g if the snake head is at the right upper corner of the whole panel,
    // X_COORDINATES[0] = (HORIZONTAL_BLOCKS - 1) * UNIT_SIZE = 19 * 30
    // Y_COORDINATES[0] = 0 * UNIT_SIZE = 0

    // X_COORDINATES: x coordinates of all body parts of the snake
    // X_COORDINATES[0] means the x coordinate of the head of the snake
    // first "bodyParts" amount of elements are useful, rest of the elements in the array can be ignored
    final int[] X_COORDINATES = new int[TOTAL_NUMBER_OF_BLOCKS];
    final int[] Y_COORDINATES = new int[TOTAL_NUMBER_OF_BLOCKS];

    int bodyParts = 6;
    int applesEaten;
    int applePositionX;
    int applePositionY;
    char direction = 'R'; // L: left, R: right, U: up, D: down
    Boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newApple() {
        applePositionX = random.nextInt(HORIZONTAL_BLOCKS) * UNIT_SIZE;
        applePositionY = random.nextInt(VERTICAL_BLOCKS) * UNIT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // add grid effect into the background
        g.setColor(Color.gray);
        for (int i = 0; i < HORIZONTAL_BLOCKS; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        for (int i = 0; i < VERTICAL_BLOCKS; i++) {
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
        // add a new apple
        g.setColor(Color.red);
        g.fillOval(applePositionX, applePositionY, UNIT_SIZE, UNIT_SIZE);
        // draw the snake
        // draw head, head's colour be different from the rest of the body
        g.setColor(Color.green);
        g.fillRect(X_COORDINATES[0], Y_COORDINATES[0], UNIT_SIZE, UNIT_SIZE);
        // draw other parts of the body
        for (int i = 1; i < bodyParts; i++) {
            // If you want a green snake, use the commented-out code below, and get rid of the multicolour code
            // g.setColor(new Color(45, 180, 0));
            // Multi-colour snake
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.fillRect(X_COORDINATES[i], Y_COORDINATES[i], UNIT_SIZE, UNIT_SIZE);
        }
        // Score displayed
        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten,
                (SCREEN_WIDTH - scoreMetrics.stringWidth("Score: "+ applesEaten)) / 2,
                scoreMetrics.getHeight());
        // Display "Game Over" if the game finishes
        if (!running) gameOver(g);
    }

    // all body parts move 1 block forward every time the method being called
    public void move() {
        // every body part's current position should be the before position of the body part before it
        for (int i = bodyParts - 1; i > 0; i--) {
            X_COORDINATES[i] = X_COORDINATES[i-1];
            Y_COORDINATES[i] = Y_COORDINATES[i-1];
        }
        // rewrite the new position of the head of the snake
        switch (direction) {
            case 'L':
                X_COORDINATES[0] = X_COORDINATES[0] - UNIT_SIZE;
                break;
            case 'R':
                X_COORDINATES[0] = X_COORDINATES[0] + UNIT_SIZE;
                break;
            case 'U':
                Y_COORDINATES[0] = Y_COORDINATES[0] - UNIT_SIZE;
                break;
            case 'D':
                Y_COORDINATES[0] = Y_COORDINATES[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (X_COORDINATES[0] == applePositionX && Y_COORDINATES[0] == applePositionY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        int head_X = X_COORDINATES[0];
        int head_Y = Y_COORDINATES[0];
        // check if snake's head collides with its body
        for (int i = bodyParts - 1; i > 0; i--) {
            if (head_X == X_COORDINATES[i] && head_Y == Y_COORDINATES[i]) {
                running = false;
                timer.stop();
                return;
            }
        }
        // check if snake's head collides with the wall
        if (head_X < 0 || head_X >= SCREEN_WIDTH || head_Y < 0 || head_Y >= SCREEN_HEIGHT) {
            running = false;
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        FontMetrics textMetrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - textMetrics.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
