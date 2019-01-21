package screens;

import spaceAttack.GamePane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author David Bermejo Simon
 **/
public class StartScreen extends JPanel implements Screen, ActionListener {


    private static final String BACKGROUND_WELCOME = "resources/images/backgroundWelcome.jpg";
    private static final int INTERLINE_SPACE = 30;

    GamePane gamePane;
//    JButton selectionTieFighter;
//    JButton selectionXWing;
//    JButton selectionStandardShip;

    JButton[] buttons;

    //    //PARAMETROS DE CONTROL
    Image backgroundImage;


    public StartScreen(GamePane gamePane) {
        this.gamePane = gamePane;
        startFrame();
    }


    @Override
    public void startFrame() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints settings = new GridBagConstraints();

//        selectionStandardShip = new JButton("Standard Ship");
//        selectionTieFighter = new JButton("Tie-Fighter");
//        selectionXWing = new JButton("X-Wing");
//
//        buttons = new JButton[3];
//
//        this.buttons[0] = selectionStandardShip;
//        this.buttons[1] = selectionTieFighter;
//        this.buttons[2] = selectionXWing;
//
//        for (int i = 0; i < buttons.length; i++) {
//            buttons[i].addActionListener(this);
//            settings = new GridBagConstraints();
//            settings.gridx = 0;
//            settings.gridy = i;
//            settings.anchor = GridBagConstraints.WEST;
//            this.add(buttons[i], settings);
//        }

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
        g.drawString("Space Attack!", gamePane.getWidth() / 2, gamePane.getHeight() / 2);
        g.drawString("Haz click para comenzar", gamePane.getWidth() / 2, gamePane.getHeight() / 2 + INTERLINE_SPACE);
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
        gamePane.setEndLevel(true);
    }


    //GESTION DE LOGICA DE LOS BOTONES DE SELECCION DE NAVE
    @Override
    public void actionPerformed(ActionEvent e) {
//        for (JButton button : buttons) {
//            if (e.getSource() == selectionStandardShip) {
//                gamePane.setSpaceShipOption(0);
//            }
//            if (e.getSource() == selectionXWing) {
//                gamePane.setSpaceShipOption(1);
//            }
//            if (e.getSource() == selectionTieFighter) {
//                gamePane.setSpaceShipOption(2);
//            }
//        }

    }
}
