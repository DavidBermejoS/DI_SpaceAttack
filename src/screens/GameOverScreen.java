package screens;

import spaceAttack.GamePane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author David Bermejo Simon
 **/
public class GameOverScreen extends JPanel implements Screen{


    private static final String BACKGROUND_GAMEOVER = "resources/images/gameOver.png";
    Color fontsColor = new Color(255,255,255,50);
    private static final int INTERLINE_SPACE = 30;

    //    //PARAMETROS DE CONTROL
    GamePane gamePane;
    Image backgroundImage;


    public GameOverScreen(GamePane gamePane) {
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
        manageGameFunctions();
    }

    public void manageGameFunctions() throws InterruptedException {
        //no hace nada
    }

    /**
     * Metodo encargado de pintar el fondo del panel de juego
     *
     * @param g
     */
    private void drawBackground(Graphics g) {
        File bckg;
        bckg = new File(BACKGROUND_GAMEOVER);
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
        g.setFont(new Font("MonoSpace", Font.BOLD, 32));
        g.setColor(fontsColor);
        g.drawString("HAZ CLICK PARA VOLVER AL INICIO", gamePane.getWidth() / 4,gamePane.getHeight() /4);
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
        gamePane.setEndLevel(true);
        gamePane.setGameOver(false);
        gamePane.setActualLevel(-1);

    }

}
