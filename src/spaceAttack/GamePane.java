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
public class GamePane extends JPanel implements Runnable, MouseMotionListener, MouseListener, KeyListener {

    final static String[] HANGAR = {"resources/hangar/jediFighterGreen.png", "resources/hangar/xWing.png", "resources/hangar/tieFighter.png"};

    int shipSelected;

    public boolean endLevel;
    private int actualLevel;
    public Screen screen;
    private boolean gameOver;

    public double score;


    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public GamePane() {
        actualLevel = -1;
        shipSelected = 0;
        score = 0;
        this.endLevel = true;
        this.gameOver = false;
        this.setFocusable(true);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addKeyListener(this);
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

    //GESTION DEL HILO
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
                    this.screen = new StartScreen(this);
                    endLevel = false;
                    break;
                case 1:
                    this.screen = new FirstScreen(this, HANGAR[shipSelected]);
                    endLevel = false;
                    break;
                case 2:
                    this.screen = new SecondScreen(this, HANGAR[shipSelected]);
                    endLevel = false;
                    break;
                case 3:
                    this.screen = new VictoryScreen(this);
                    endLevel = false;
                    break;
            }

        } else if (endLevel && gameOver) {
            this.screen = new GameOverScreen(this);
            actualLevel = -1;
            endLevel = false;

        }
    }


    //METODOS SET DE VARIABLES DE CONTROL
    public void setEndLevel(boolean endLevel) {
        this.endLevel = endLevel;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setActualLevel(int actualLevel) {
        this.actualLevel = actualLevel;
    }

    /**
     * Metodo para establecer desde la pantalla de inicio que nave querremos manejar
     * durante el juego
     *
     * @param i
     */
    public void setSpaceShipOption(int i) {
        shipSelected = i;
    }

    public void setActualScore(double i) {
        this.score += i;
    }

    public String getActualScore() {
        return String.valueOf(score);
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

    //EVENTOS DE TECLADO
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        this.screen.keyPressed(keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

}


