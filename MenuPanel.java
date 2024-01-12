import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The class is a JPanel with the neceessary component for the game's menu.
 * Any behavior related to the game's menu panel is definede here.
 * @author Wesley Lukman
 * @version 02 August 2023
 */
public class MenuPanel extends JPanel implements ActionListener {
    private JButton play;
    private JButton exit;
    private NextInterface panel;
    private Dimension d;

    /**
     * It is my custom functional interface to allow method referencing.
     */
    @FunctionalInterface
    interface NextInterface {
        void next();
    }

    /**
     * The constructor construct a new MenuPanel based on the given parameters.
     * @param d It is the dimension of the MenuPanel.
     * @param text It is the title of the menu.
     * @param textCol It is the color of the title.
     * @param panel It is a function pointer to a function that allow panel switching. 
     */
    public MenuPanel(Dimension d, String text, Color textCol, NextInterface panel) {
        /**Setting the Dimension**/
        this.d = d;
        /**Setting for switchiing panels**/
        this.panel = panel;
        /**Setting the necessary sub-panel**/
        JPanel title = titlePanel(text, textCol);
        JPanel button = buttonPanel();
        /**constructing the panel**/
        setPreferredSize(d);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_START;

        gbc.insets = new Insets(130,0,0,0);
        add(title, gbc);

        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridy = 200;
        gbc.insets = new Insets(44,0,0,0);
        add(button, gbc); 
    }

    /**
     * The method constructs the exit and play button.
     */
    private void buttonConstruct() {
        /**exit**/
        exit = new JButton("exit");
        exit.setFont(new Font("exit", Font.BOLD, 30));
        exit.setBackground(new Color(180,13,26));
        exit.setForeground(new Color(237,244,234));
        exit.addActionListener(MenuPanel.this);
        /**play**/
        play = new JButton("play");
        play.setFont(new Font("play", Font.BOLD, 30));
        play.setBackground(new Color(248,199,101));
        play.setForeground(new Color(75,38,55));
        play.addActionListener(MenuPanel.this);
    }

    /**
     * The method creates a JPanel consisting of the exit and play button.
     * @return The desired JPanel.
     */
    private JPanel buttonPanel() {
        buttonConstruct();
        /**layout construction**/
        GridLayout layout  = new GridLayout(2, 1);
        layout.setVgap(20);
        /**Constructing the button panel**/
        JPanel b = new JPanel();
        b.setLayout(layout);
        b.setPreferredSize(new Dimension(150, 100));
        b.add(play);
        b.add(exit);
        b.setMinimumSize(new Dimension(134,104));
        return b;
    }

    /**
     * The method creates a JPanel consisting of a title.
     * @param text The string that is to be the title.
     * @return The desired JPanel.
     */
    private JPanel titlePanel(String text, Color col) {
        /**Font contruction**/
        Font font = new Font("mine", Font.ITALIC,100);
        /**title construction**/
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setFont(font);
        title.setForeground(col);      
        /**panel construction**/
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(600, 150));
        p.add(title, BorderLayout.CENTER);
        return p;
    }

    /**
    *The method manages the event related to the exit and play button.
    *@param e It is the event.
    **/   
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) 
            System.exit(0);
        if (e.getSource() == play) 
            panel.next();
    } 
}