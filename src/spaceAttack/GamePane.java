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

    final static String [] HANGAR = {"resources/images/nave.png","resources/images/xWing.png"  ,"resources/images/tieFighter.png" };

    int shipSelected;

    public boolean endLevel;
    private int actualLevel;
    public Screen screen;
    private boolean gameOver;



    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public GamePane() {
        actualLevel = -1;
        shipSelected = 0;
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
            endLevel = false;
            switch (actualLevel) {
                case 0:
                    this.actualLevel = 0;
                    this.screen = new StartScreen(this);
                    break;
                case 1:
                    this.actualLevel=1;
                    this.screen = new FirstScreen(this,HANGAR[shipSelected]);
                    break;
                case 2:
                    this.actualLevel=2;
                    this.screen = new SecondScreen(this,HANGAR[shipSelected]);
                    break;
                case 3:
                    this.actualLevel = 3;
                    this.screen = new VictoryScreen(this);
                    break;
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

    /**
     * Metodo para establecer desde la pantalla de inicio que nave querremos manejar
     * durante el juego
     * @param i
     */
    public void setSpaceShipOption(int i) {
        shipSelected = i;
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

