package screens;

import spaceAttack.GamePane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author David Bermejo Simon
 **/
public class StartScreen extends JPanel implements Screen{


    private static final String BACKGROUND_WELCOME = "resources/images/backgroundWelcome.jpg";
    private static final int INTERLINE_SPACE = 30;

    GamePane gamePane;
    //    //PARAMETROS DE CONTROL
    Image backgroundImage;


    public StartScreen(GamePane gamePane) {
        this.gamePane = gamePane;
    }



    @Override
    public void startFrame() {
        //
    }

    @Override
    public void startScreen() {
        //
    }

    @Override
    public void drawScreen(Graphics g) throws InterruptedException {
        drawBackground(g);
        drawAnimationScreen(g);
        manageGraphics();
    }

    public void manageGraphics() throws InterruptedException {
        //en este caso lo unico que gestiona el manageGraphics es la tasa de refresco
        Thread.currentThread().sleep(250);
    }

    /**
     * Metodo encargado de pintar el fondo del panel de juego
     *
     * @param g
     */
    private void drawBackground(Graphics g) {
        File bckg;
        bckg = new File(BACKGROUND_WELCOME);
        try {
            backgroundImage = ImageIO.read(bckg);
            backgroundImage = backgroundImage.getScaledInstance(gamePane.getWidth(), gamePane.getHeight(), 4);
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen de fondo");
            System.out.println("Error: " + e.getMessage());
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }


    /**
     * Metodo encargado de gestionar la animacion de la pantalla principal
     */
    private void drawAnimationScreen(Graphics g) {
        Random r = new Random();
        int red = 0;
        int green = 0;
        int blue = 0;
        red = r.nextInt(256);
        green = r.nextInt(256);
        blue = r.nextInt(256);
        g.setFont(new Font("MonoSpace", Font.BOLD, 24));
        g.setColor(new Color(red, green, blue));
        g.drawString("Space Attack!", gamePane.getWidth() / 2, gamePane.getHeight() / 2);
        g.drawString("Haz click para comenzar", gamePane.getWidth() / 2, gamePane.getHeight() / 2 + INTERLINE_SPACE);
        g.dispose();
    }
    @Override
    public void resizeScreen(Graphics g) {
        backgroundImage = backgroundImage.getScaledInstance(gamePane.getWidth(), gamePane.getHeight(), 4);
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }


    @Override
    public void moveMouse(MouseEvent e) {

    }

    @Override
    public void clickMouse(MouseEvent e) {
        gamePane.endLevel=true;
    }

}
