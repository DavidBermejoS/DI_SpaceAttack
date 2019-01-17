package spaceAttack;

import screens.GameScreen;
import screens.Screen;
import screens.StartScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Clase GamePane
 * Esta clase gestiona el panel de juego junto al personaje y los elementos que interactuan en el juego
 *
 * @author David Bermejo Simon
 **/
public class GamePane extends JPanel implements Runnable, MouseMotionListener, MouseListener {

    public boolean endLevel;
    private int actualLevel;
    Screen screen;

    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public GamePane() {
        actualLevel = 0;
        this.endLevel = false;
        this.screen = new StartScreen(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            this.screen.drawScreen(g);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.currentThread().sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toolkit.getDefaultToolkit().sync();
            checkLevel();
        }
    }

    private void checkLevel() {
        if (endLevel) {
            actualLevel++;
            switch (actualLevel) {
                case 1:
                    this.screen = new GameScreen(this);
                    endLevel = false;
                    break;
                case 2:
                    this.screen = new GameScreen(this);
                    endLevel = false;
                    break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }


    @Override
    public void mouseMoved(MouseEvent e) {
        this.screen.moveMouse(e);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        //no hace nada
    }


    @Override
    public void mousePressed(MouseEvent e) {
        this.screen.clickMouse(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //no hace nada
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //no hace nada
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //no hace nada
    }
}

