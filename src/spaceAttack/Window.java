package spaceAttack;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Bermejo Simon
 **/
public class Window {
    JFrame frame;
    GamePane gamePane;
    Sprite sprite;


    public Window(){
        frame = new JFrame("Actividad 1 UT9");
        frame.setBounds(400,300,400,400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public void addComponents(){
        this.frame.setLayout(new GridLayout());
        this.gamePane = new GamePane();
        frame.add(gamePane);
    }



    public void addListeners(){

    }


    public void inicializate(){
        addComponents();
        addListeners();
        this.frame.setVisible(true);

    }
}
