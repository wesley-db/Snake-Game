import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The class is the game panel itself.
 * Any behavior related to the game itself is defined in this class.
 * @author Wesley Lukman
 * @version 02 August 2023
 */
public class SnakePanel extends JPanel implements ActionListener {
    /*For changing to the next card**/
    private NextInterface panel;
    /*screen's dimension variable**/
    private Dimension d;
    private final int UNIT_SIZE = 40;
    /*Apple variables**/
    private Point applePoint;
    private boolean appleEaten;
    /*Snake variables**/
    private LinkedList<Point> snakePoint;
    private int INITIAL_LENGTH = 4;
    /*Direction**/
    private final char UP = 'U';
    private final char LEFT = 'L';
    private final char RIGHT = 'R';
    private final char DOWN = 'D';
    private char dir;
    /*For automatic operation**/
    private Timer time;
    /*Preventing pressing key at the same time***/
    boolean pressed;

    /**
     * It is my custom functional interface to allow method referencing.
     */
    @FunctionalInterface
    interface NextInterface {
        void next();
    }

    /**
     *The constructor construct a SnakePanel.
     *@param width It is the width of the SnakePanel.
     *@param height It is the height of the SnakePanel.
     *@param speed It is how fast the snake is moving.
     *@param panel It is a function pointer to a function that allow panel switching. 
     */
    public SnakePanel (int width, int height, int speed, NextInterface panel) {
        /**Setting for switching panels**/
        this.panel = panel;
        /**Setting the Dimension**/
        d = new Dimension(width, height);
        setPreferredSize(d);
        /**Setting the background color**/
        setBackground(new Color(48,38,33));
        /**Set timer**/
        time = new Timer(speed, this);
        /**Initializing the apple**/
        newApple();
        appleEaten = false;
        /**Initializing the snake**/
        initiateSnakePoint();
        dir = RIGHT;
        /**Settings to let keyboard inputs**/
        this.addKeyListener(new DummyKeyAdapter());
        pressed = false;
    }

    /**
     * The method initiate the snake game.
     */
    public void start() {
        /**start timer**/
        time.start();
        /**Request focusable**/
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * The method paints the game's screen as desired.
     * @param g The desired Graphics object.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //drawGrid(g);
        /**drawing apple**/
        g.setColor(new Color(255,0,0));
        g.drawRoundRect(applePoint.x, applePoint.y, UNIT_SIZE, UNIT_SIZE,24,34);
        g.fillRoundRect(applePoint.x, applePoint.y, UNIT_SIZE, UNIT_SIZE,24,34);
        /**drawing snake**/
        Point dummy;
        g.setColor(new Color(138,190,97));
        for (int i=snakePoint.size()-1; i>0; i--) {
            dummy = snakePoint.get(i);
            g.drawRect(dummy.x,dummy.y,UNIT_SIZE,UNIT_SIZE);
            g.fillRect(dummy.x,dummy.y,UNIT_SIZE,UNIT_SIZE);
        }
        dummy = snakePoint.peekFirst();
        g.setColor(new Color(56,133,93));
        g.drawRect(dummy.x,dummy.y,UNIT_SIZE,UNIT_SIZE);
        g.fillRect(dummy.x,dummy.y,UNIT_SIZE,UNIT_SIZE);
    }

    /**
     * The method paints the game's grid on the scren.
     * @param g The desired Graphics object.
     */
    private void drawGrid(Graphics g) {
        g.setColor(new Color(203,189,173));
        for(int i = UNIT_SIZE; i < d.height; i += UNIT_SIZE) {
            g.drawLine(0, i, d.width, i);
            g.drawLine(i, 0, i, d.height);
        }
    }

    /**
     *The method creates a new apple.
     */
    private void newApple() {
        Random rand = new Random();
        int xApple = rand.nextInt((d.width/UNIT_SIZE) - 1) * UNIT_SIZE;
        int yApple = rand.nextInt((d.height/UNIT_SIZE) - 1) * UNIT_SIZE;
        applePoint = new Point(xApple,yApple);
    }

    /**
     * The method set the snake's size and location to default.
     */
    private void initiateSnakePoint() {
        if (snakePoint == null)
            snakePoint = new LinkedList<>();
        else 
            snakePoint.clear(); 
        /*********************************/
        for (int i=INITIAL_LENGTH-1; i>=0; i--) {
            snakePoint.add(new Point(i * UNIT_SIZE,0));
        }
    }

    /**
     * The method defines the snake's movement.
     */
    private void move() {
        if (!appleEaten) 
            snakePoint.removeLast();
        /*************************/
        Point prevPoint = snakePoint.peekFirst();
        switch (dir) {
            case UP:
                snakePoint.addFirst(new Point(prevPoint.x, prevPoint.y-UNIT_SIZE));
                break;
            case DOWN:
                snakePoint.addFirst(new Point(prevPoint.x, prevPoint.y+UNIT_SIZE));
                break;
            case LEFT:
                snakePoint.addFirst(new Point(prevPoint.x-UNIT_SIZE, prevPoint.y));
                break;
            case RIGHT:
                snakePoint.addFirst(new Point(prevPoint.x+UNIT_SIZE, prevPoint.y));
                break;
        }
    }

    /**
     * The method check if the snake's head collide with either a wall or itself.
     * If some sort of collision occured, it restart the game and show the game over menu panel.
     */
    private void collisionCheck() {
        if (wallCheck() || selfCheck()) {
            /****Returning everything to how it is at the begining****/
            time.stop();
            initiateSnakePoint();
            newApple();
            dir = RIGHT;
            appleEaten = false;
            /****Show game over menu****/
            panel.next();
        }
    }

    /**
     * The method check if the snake's head collides with a wall.
     * @return It return true when the snake does collide, and false otherwise.
     */
    private boolean wallCheck() {
        Point headPoint = snakePoint.peekFirst();
        boolean checkX = headPoint.x>=d.width || headPoint.x<0;
        boolean checkY = headPoint.y>=d.height || headPoint.y<0; 
        return checkX || checkY;
    }

    /**
     * The method check if the snake's head collides with a wall.
     * @return It return true when the snake does collide, and false otherwise.
     */
    private boolean selfCheck() {
        boolean answ = false;
        Point headPoint = snakePoint.peekFirst();
        for (int i=snakePoint.size()-1; i>0; i--) {
            if (snakePoint.get(i).equals(headPoint)) {
                answ = true;
                break;
            }
        }
        return answ;
    }

    /**
     * The method checks if the snake ate an apple, and act as appropriate.
     */
    private void appleCheck() {
        if (snakePoint.peekFirst().equals(applePoint)) {
            appleEaten = true;
            newApple();
        }
        else 
            appleEaten = false;
    }

    /**
     * The method defines the action to be performed when an event occured.
     * @param e The ActionEvent object which tells what event occured.
     */
    public void actionPerformed(ActionEvent e) {
        pressed = false;
        collisionCheck();
        move();
        appleCheck();   
        repaint();
    }

    /**
     * The class allows the game to take in keyboard inputs.
     */
    public class DummyKeyAdapter extends KeyAdapter {
        /**
         * The method defines the action to be taken when a keyboard key is pressed.
         * @param e It is a KeyEvent object which tells what keyboard key is pressed.
         */
        @Override public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) 
                System.exit(0);
            if (e.getKeyCode()==KeyEvent.VK_LEFT && dir!=RIGHT && !pressed) 
                dir = LEFT;
            if (e.getKeyCode()==KeyEvent.VK_RIGHT && dir!=LEFT && !pressed) 
                dir = RIGHT;
            if (e.getKeyCode()==KeyEvent.VK_UP && dir!=DOWN && !pressed) 
                dir = UP;
            if (e.getKeyCode()==KeyEvent.VK_DOWN && dir!=UP && !pressed) 
                dir = DOWN;
            /**Reset pressed**/
            pressed = true;
        }
    }
}
