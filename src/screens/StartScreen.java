package screens;

import spaceAttack.GamePane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author David Bermejo Simon
 **/
public class StartScreen extends JPanel implements Screen {


    private static final String BACKGROUND_WELCOME = "resources/images/backgroundWelcome.jpg";
    private static final int INTERLINE_SPACE = 30;

    GamePane gamePane;
    //    //PARAMETROS DE CONTROL
    Image backgroundImage;


    public StartScreen(GamePane gamePane) {
        this.gamePane = gamePane;
        startFrame();
    }


    @Override
    public void startFrame() {
        this.setFocusable(true);
    }


    @Override
    public void startScreen() {
        //
    }

    //METODOS ENCARGADOS DE GESTIONAR LOS GRAFICOS DEL JUEGO
    @Override
    public void drawScreen(Graphics g) throws InterruptedException {
        drawBackground(g);
        drawAnimationScreen(g);
        manageGameFunctions();
    }

    public void manageGameFunctions() throws InterruptedException {
        //en este caso lo unico que gestiona el manageGameFunctions es la tasa de refresco
        Thread.currentThread().sleep(100);
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
        g.drawString("Space Attack!", gamePane.getWidth() / 3, gamePane.getHeight() / 2);
        g.drawString("Pulsa alguno de los botones para elegir nave y comenzar",
                gamePane.getWidth() / 3,
                gamePane.getHeight() / 2 + INTERLINE_SPACE*2);

        g.drawString("1 - Caza Estelar Jedi",
                gamePane.getWidth() / 3,
                gamePane.getHeight() / 2 + INTERLINE_SPACE*3);

        g.drawString("2 - Ala X",
                gamePane.getWidth() / 3,
                gamePane.getHeight() / 2 + INTERLINE_SPACE*4);

        g.drawString("3 - Caza Tie",
                gamePane.getWidth() / 3,
                gamePane.getHeight() / 2 + INTERLINE_SPACE*5);
        g.dispose();
    }

    @Override
    public void resizeScreen(Graphics g) {
        backgroundImage = backgroundImage.getScaledInstance(gamePane.getWidth(), gamePane.getHeight(), 4);
    }


    //METODO GET DEL COMPONENTE GRAPHICS DE ESTE SCREEN
    @Override
    public Graphics getGraphics() {
        return null;
    }


    //METODOS DE GESTION DE EVENTOS DE RATON
    @Override
    public void moveMouse(MouseEvent e) {

    }

    @Override
    public void clickMouse(MouseEvent e) {
        // no hace nada
    }

    /**
     * Metodo para selección de nave en pantalla principal
     * @param e
     */
    public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F1) {
                JOptionPane.showMessageDialog(gamePane, "Has elegido el Caza Jedi");
                gamePane.setEndLevel(true);
                gamePane.setGameOver(false);
                gamePane.setSpaceShipOption(0);

            }
            if (e.getKeyCode() == KeyEvent.VK_F2) {
                JOptionPane.showMessageDialog(gamePane, "Has elegido el Ala X");
                gamePane.setEndLevel(true);
                gamePane.setGameOver(false);
                gamePane.setSpaceShipOption(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_F3) {
                JOptionPane.showMessageDialog(gamePane, "Has elegido el Caza Tie");
                gamePane.setEndLevel(true);
                gamePane.setGameOver(false);
                gamePane.setSpaceShipOption(2);
            }
        }
}
