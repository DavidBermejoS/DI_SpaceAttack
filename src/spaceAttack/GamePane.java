package spaceAttack;

import screens.*;

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
    public Screen screen;
    private boolean gameOver;

    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public GamePane() {
        actualLevel = -1;
        this.endLevel = true;
        this.gameOver = false;
//        this.screen = new StartScreen(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        new Thread(this).start();
    }

    //GESTION DE LA EJECUCION DEL JUEGO EN EL PANEL GAMEPANE
    @Override
    protected void paintComponent(Graphics g) {
        try {
            this.screen.drawScreen(g);
        } catch (InterruptedException e) {
            System.out.println("Se ha producido un error al cargar la nueva pantalla");
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            checkLevel();
            repaint();
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toolkit.getDefaultToolkit().sync();
        }
    }

    //MENU CON LAS DISTINTAS PANTALLAS Y OPCIONES DE CARGA
    /**
     * Metodo encargado de checkear en todo momento si es final del juego
     * para gestionar el cambio de pantallas
     */
    private void checkLevel() {
        if (endLevel && !gameOver) {
            actualLevel++;
            switch (actualLevel) {
                case 0:
                    endLevel = false;
                    this.screen = new StartScreen(this);
                    break;
                case 1:
                    endLevel = false;
                    this.screen = new FirstScreen(this);
                    break;
                case 2:
                    endLevel = false;
                    this.screen = new SecondScreen(this);
                    break;
                case 3:
                    actualLevel=-1;
                    endLevel = false;
                    this.screen = new GameOverScreen(this);
            }

        }else if(endLevel && gameOver){
            this.screen = new GameOverScreen(this);
            actualLevel=-1;
            endLevel = false;

        }
    }


    //METODOS GET Y SET DE VARIABLES DE CONTROL
    public boolean isEndLevel() {
        return endLevel;
    }

    public void setEndLevel(boolean endLevel) {
        this.endLevel = endLevel;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getActualLevel() {
        return actualLevel;
    }

    public void setActualLevel(int actualLevel) {
        this.actualLevel = actualLevel;
    }

    //EVENTOS DE RATON
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

