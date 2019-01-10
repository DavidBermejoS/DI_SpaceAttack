package spaceAttack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * @author David Bermejo Simon
 **/
public class GamePane extends JPanel implements Runnable {

    ArrayList<Sprite> sprites;
    CollisionCounter counter;
    int numSprites;

    //imagen de fondo
    Image backgroundImage;

    String asteroidImage = "resources/images/asteroide.png";

    public GamePane() {
        this.numSprites = 0;
        this.setLayout(new GridBagLayout());
        sprites = new ArrayList<>();
        addListeners();
        addCounter();
        new Thread(this).start();

    }


    @Override
    protected void paintComponent(Graphics g) {
        fillBackground(g);
        drawSprite(g);
    }


    /**
     * Metodo encargado de pintar los sprites contenidos en la lista sprites.
     *
     * @param g
     * @see Sprite
     */
    private void drawSprite(Graphics g) {
        for (Sprite s : sprites) {
            g.drawImage(
                    s.getBuffer(),
                    s.getPosX(),
                    s.getPosY(),
                    s.getAncho(),
                    s.getAlto(),
                    s.getColor(),
                    null
            );
        }
    }


    /**
     * Metodo encargado de pintar el fondo del panel de juego
     *
     * @param g
     */
    private void fillBackground(Graphics g) {
        File bckg = new File("resources/images/background.jpg");
        backgroundImage = obtainImage(bckg);
        backgroundImage = scaleImage(backgroundImage);
        g.drawImage(backgroundImage, 0, 0, null);
    }

    /**
     * Metodo para cargar en memoria las imagenes que necesitemos
     *
     * @param file
     */
    private BufferedImage obtainImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Metodo para reescalar imagenes
     *
     * @param image : imagen que se redimensionara.
     */
    private Image scaleImage(Image image) {
        return image.getScaledInstance(this.getWidth(), this.getHeight(), 4);
    }


    /**
     * Metodo encargado de añadir un listener al panel de juego para
     * el control del mouse.
     */
    private void addListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Random rd = new Random();
                Sprite sprite = new Sprite();
                sprite.setPosX(e.getX());
                sprite.setPosY(e.getY());
                sprite.setAncho(30);
                sprite.setAlto(30);
                sprite.setVx(rd.nextInt(6)+1);
                sprite.setVy(rd.nextInt(6)+1);
                sprite.setFileImage(new File(asteroidImage));
                sprite.refreshBuffer();
                sprite.setIdSprite(numSprites);
                sprites.add(sprite);
            }
        });
        numSprites++;
    }


    /**
     * Metodo encargado de anadir un contador de colisiones de sprites al panel de juego
     */
    private void addCounter() {
        this.counter = new CollisionCounter();
        GridBagConstraints settings = new GridBagConstraints();
        settings.weightx = 5;
        settings.weighty = 5;
        settings.ipadx = 30;
        settings.ipady = 10;
        settings.anchor = GridBagConstraints.SOUTHEAST;
        this.add(counter, settings);
    }


    /**
     * Metodo encargado de comprobar las colisiones con las paredes de la ventana y cambiar la velocidad
     * en caso de que exista dicha colision
     *
     * @param sprite
     */
    private void checkCollision(Sprite sprite) {
        if (sprite.getPosX() <= 0) {
            sprite.setVx(Math.abs(sprite.getVx()));
        } else if (sprite.getPosX() >= this.getWidth() - sprite.getAncho()) {
            sprite.setVx(Math.abs(sprite.getVx()) * -1);
        }

        if (sprite.getPosY() <= 0) {
            sprite.setVy(Math.abs(sprite.getVy()));
        } else if (sprite.getPosY() >= this.getHeight() - sprite.getAlto()) {
            sprite.setVy(Math.abs(sprite.getVy()) * -1);
        }
    }


    /**
     * Metodo encargado de comprobar si dos sprites colisionan entre si.
     * Si estos colisionan entre si, se marcara su atributo destroyed a true.
     */
    private void checkSpritesCollision() {
        for (Sprite s1 : sprites) {
            for (Sprite s2 : sprites) {
                if (s1 != s2) {
                    if (s1.isCollides(s2)) {
                        s1.changeVelocity();
                        s2.changeVelocity();

                    }
                }
            }
        }
    }

    /**
     * Metodo encargado de crear una copia de la lista de sprites. En esta copia se eliminarán los sprites colisionados.
     * Posteriormente la lista original se actualizará con la información de la copia.
     */
//    private void destroyCollisionedSprites() {
//        ArrayList<Sprite> spritesAux = (ArrayList<Sprite>) sprites.clone();
//        for (Sprite s : sprites) {
//            if (s.isDestroyed()) {
//                spritesAux.remove(s);
//                this.counter.plusCollision();
//            }
//        }
//        sprites = (ArrayList<Sprite>) spritesAux.clone();
//    }


    @Override
    public void run() {
        while (true) {

            try {
                sleep(25);
                for (Sprite s : sprites) {
                    s.setPosX(s.getPosX() + s.getVx());
                    s.setPosY(s.getPosY() + s.getVy());
                    checkCollision(s);
                    checkSpritesCollision();
                }
                this.counter.refreshCounter();
                repaint();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
