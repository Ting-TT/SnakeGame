import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int TOTAL_NUMBER_OF_UNITS = SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE; // ???????????????
    static final int HORIZONTAL_BLOCKS = SCREEN_WIDTH / UNIT_SIZE;
    static final int VERTICAL_BLOCKS = SCREEN_HEIGHT/ UNIT_SIZE;
    static final int DELAY = 75; // a delay for the timer
    final int[] x = new int[TOTAL_NUMBER_OF_UNITS]; // ???????????????
    final int[] y = new int[TOTAL_NUMBER_OF_UNITS]; // ???????????????
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

    }
    public void newApple() {
        applePositionX = random.nextInt(HORIZONTAL_BLOCKS) * UNIT_SIZE;
        applePositionY = random.nextInt(VERTICAL_BLOCKS) * UNIT_SIZE;

    }
    public void move() {

    }
    public void checkApple() {

    }
    public void checkCollisions() {

    }
    public void gameOver() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
        }
    }
}
