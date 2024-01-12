/**The class is where the snake game's main method is located.
 * The snake is controlled with the up, left, right, and down key on the keyboard.
 * The rules of the game is the same as the usual.
 * @author Wesley Lukman
 * @version 02 August 2023
 */
public class SnakeGame {
    /**
     * The method is the main method of the snake game's code.
     * @param args It is an array of strings from the operating system.
     */
    public static void main(String[] args) {
        GameFrame frame = new GameFrame(600, 640);
        frame.start();
    }

}