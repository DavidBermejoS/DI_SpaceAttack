package spaceAttack;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author David Bermejo Simon
 **/
public class Window {
    JFrame frame;
    GamePane gamePane;

    public Window() {
        frame = new JFrame("Actividad 1 UT9");
        frame.setBounds(400, 400, 1200, 850);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public void addComponents() {
        this.frame.setLayout(new GridLayout());
        this.gamePane = new GamePane();
        frame.add(gamePane);


        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg,
                new Point(0, 0),
                "blank cursor"
        );

        // Set the blank cursor to the JFrame.
        frame.getContentPane().setCursor(blankCursor);
    }


    public void addListeners() {

    }


    public void inicializate() {
        addComponents();
        addListeners();
        this.frame.setVisible(true);

    }
}
