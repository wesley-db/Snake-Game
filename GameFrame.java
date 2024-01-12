import java.awt.Color;
import java.awt.Point;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The class is where all the components of the game come together.
 * Its main purpose is to bring all components together to produce the game.
 * @author Wesley Lukman
 * @version 02 August 2023
 */
public class GameFrame extends JFrame {
    private Dimension d;
    private MenuPanel main;
    private MenuPanel gameOver;
    private SnakePanel game;
    private JPanel ancestor;

    /**
     * The construct constructs a new GameFrame with the given parameter.
     * It sets all the components needed for the game.
     * @param width It is the width of the game's frame.
     * @param height It is the height of the game's frame.
     */
    public GameFrame(int width, int height) {
        ImageIcon img = new ImageIcon("../Icon/Icon.png");
        this.setIconImage(img.getImage()); 
        /****Setting the dimension****/
        d = new Dimension(width, height);
        //extra space is needed for the borders and title bar
        //so the frame size will be larger than the panels
        //setPreferredSize(d);
        setMinimumSize(d);
        /****Creating Frame****/
        setTitle("Snake");
        /****Setting resizable****/
        setResizable(false);
        /****Creating the necessary JPanels****/
        ancestor = new JPanel(new CardLayout());
        ancestor.setPreferredSize(d);
        ancestor.setMinimumSize(d);
        main = new MenuPanel(d, "Snake", new Color(22, 96, 55), this::initiateGame);
        main.setMinimumSize(d);
        gameOver = new MenuPanel(d, "Game Over", new Color(191, 52, 48), this::initiateGame);
        gameOver.setMinimumSize(d);
        game = new SnakePanel(width, height, 200, this::stopGame);
        game.setMinimumSize(d);
        /****Setting default close operation****/
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        /****Set game Location on the screen****/
        this.setLocationRelativeTo(null);
    }

    /**
     * The method initiates/start all the components necessary for the game.
     */
    public void start() {
        /**Adding the necessary JPanels to ancestor**/
        ancestor.add("main", main);
        ancestor.add("game", game);
        ancestor.add("gameOver", gameOver);
        /**Adding the ancestor panel to the frame**/
        this.add(ancestor);
        this.pack();
        /**Showing the frame**/
        this.setVisible(true);
    }

    /**
     * The method starts the game.
     * The snake will only start to move when this method is called, and
     * only when this method is called, the game will be shown.
     */
    public void initiateGame() {
        CardLayout cl = (CardLayout) ancestor.getLayout();
        cl.show(ancestor, "game");
        game.start();
    }

    /**
     * The method allow the game over menu panel to be shown.
     */
    public void stopGame() {
        CardLayout cl = (CardLayout) ancestor.getLayout();
        cl.show(ancestor, "gameOver");
    }
}