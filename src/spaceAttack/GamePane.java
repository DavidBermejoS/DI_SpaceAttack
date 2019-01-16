package spaceAttack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Clase GamePane
 * Esta clase gestiona el panel de juego junto al personaje y los elementos que interactuan en el juego
 *
 * @author David Bermejo Simon
 **/
public class GamePane extends JPanel implements Runnable, MouseMotionListener, MouseListener {

    //PARAMETROS Y VALORES CONSTANTES DE LOS OBJETOS
    private static final int WIDTH_SHOOT = 20;
    private static final int HEIGHT_SHOOT = 40;
    private static final int WIDTH_ASTEROID = 40;
    private static final int HEIGHT_ASTEROID = 40;
    private static final int WIDTH_SPACESHIP = 30;
    private static final int HEIGHT_SPACESHIP = 40;
    private static final int VELOCITY_SHOOT = -20;

    //CADENAS DE TEXTO CON LAS RUTAS A LAS IMAGENES
    private static final String ASTEROID_IMAGE = "resources/images/asteroide.png";
    private static final String SPACE_SHIP_IMAGE = "resources/images/nave.png";
    private static final String LASER_IMAGE = "resources/images/laser.png";
    private static final String BACKGROUND_WELCOME = "resources/images/backgroundWelcome.jpg";
    private static final String BACKGROUND_GAME = "resources/images/background.jpg";
    private static final int INTERLINE_SPACE = 30;

    //PARAMETROS DE CONTROL
    private int numSprites;
    private double timeCount;
    private boolean shootCooldown;
    private boolean isGaming;

    ArrayList<Sprite> sprites;
    Sprite spaceShip;
    Sprite laserShoot;
    Timer timer;
    Image backgroundImage;


    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public GamePane() {
        this.numSprites = 0;
        this.isGaming = false;
        sprites = new ArrayList<>();
        new Thread(this).start();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }


    /**
     * Metodo encargado de anadir los asteroides al inicio de la partida
     */
    private void addAsteroids() {

        for (int i = 0; i < 6; i++) {
            Random rd = new Random();
            Sprite sprite = new Sprite();
            sprite.setPosX(0);
            sprite.setPosY(0);
            sprite.setAncho(WIDTH_ASTEROID);
            sprite.setAlto(HEIGHT_ASTEROID);
            sprite.setVx(rd.nextInt(6) + 1);
            sprite.setVy(rd.nextInt(6) + 1);
            sprite.setFileImage(new File(ASTEROID_IMAGE));
            sprite.refreshBuffer();
            sprite.setIdSprite(numSprites);
            sprites.add(sprite);
        }
        numSprites++;

    }

    /**
     * Metodo encargado de anadir la nave al inicio de la partida
     */
    private void addSpaceShip() {


        spaceShip = new Sprite();
        spaceShip.setAncho(WIDTH_SPACESHIP);
        spaceShip.setAlto(HEIGHT_SPACESHIP);
        spaceShip.setPosX(this.getWidth());
        spaceShip.setPosY(this.getHeight());
        spaceShip.setFileImage(new File(SPACE_SHIP_IMAGE));
        spaceShip.refreshBuffer();
        spaceShip.setIdSprite(numSprites);
        sprites.add(spaceShip);

    }


    /**
     * Metodo encargado de anadir un contador de colisiones de sprites al panel de juego
     */
    private void addTimer() {
        this.timer = new Timer(9, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeCount += 0.01;
            }
        });
        this.timer.start();
    }


    /**
     * Metodo para repintar los componentes en la pantalla
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        drawBackground(g);
        if (isGaming) {
            drawSprite(g);
            drawTimer(g);
        } else {
            drawAnimationScreen(g);
        }
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
     * Dibuja el contador de tiempo en el panel de juego
     *
     * @param g
     */
    private void drawTimer(Graphics g) {
        g.setColor(Color.RED);
        g.drawString(String.valueOf(new DecimalFormat("#.##").format(timeCount)),
                this.getWidth() - 30,
                this.getHeight() - 20);
    }


    /**
     * Metodo encargado de pintar el fondo del panel de juego
     *
     * @param g
     */
    private void drawBackground(Graphics g) {
        File bckg;
        if (isGaming) {
            bckg = new File(BACKGROUND_GAME);
        } else {
            bckg = new File(BACKGROUND_WELCOME);
        }
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
     * Metodo encargado de comprobar las colisiones con las paredes de la ventana y cambiar la velocidad
     * en caso de que exista dicha colision
     *
     * @param sprite
     */
    private void checkWallsCollision(Sprite sprite) {
        if (sprite != laserShoot) {
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
    }


    /**
     * Metodo encargado de comprobar si un asteroide colisiona con el disparo.
     * Si estos colisionan entre si, se marcara el atributo destroyed del asteroide a true.
     */
    private void checkAsteroidShooted() {
        if (shootCooldown) {
            for (int i = 0; i < sprites.size(); i++) {
                Sprite s1 = sprites.get(i);
                if (s1 != spaceShip && s1 != laserShoot) {
                    if (s1.squareCollider(laserShoot)) {
                        s1.setDestroyed(true);
                        laserShoot.setDestroyed(true);
                        this.shootCooldown = false;
                    }
                }
            }
        }
    }


    /**
     * Metodo encargado de crear una copia de la lista de sprites. En esta copia se eliminarán los sprites colisionados.
     * Posteriormente la lista original se actualizará con la información de la copia.
     */
    private void destroyCollisionedSprites() {
        ArrayList<Sprite> spritesAux = (ArrayList<Sprite>) sprites.clone();
        for (Sprite s : sprites) {
            if (s.isDestroyed()) {
                spritesAux.remove(s);
            }
        }
        sprites = (ArrayList<Sprite>) spritesAux.clone();
    }


    /**
     * Metodo que comprueba si el disparo ha abandonado los limites de la ventana para marcar el
     * enfriamiento a false para que la nave pueda volver a disparar.
     */
    private void checkCoolDown() {
        if (laserShoot.getPosY() + laserShoot.getAlto() < 0) {
            sprites.remove(laserShoot);
            this.shootCooldown = false;
        }
    }


    /**
     * Metodo encargado de actualizar la posicion de sus sprites acorde a su velocidad
     *
     * @param s : sprite a modificar.
     */
    public void moveSprites(Sprite s) {
        s.setPosX(s.getPosX() + s.getVx());
        s.setPosY(s.getPosY() + s.getVy());
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
        g.drawString("Space Attack!", this.getWidth() / 2, this.getHeight() / 2);
        g.drawString("Haz click para comenzar", this.getWidth() / 2, this.getHeight() / 2 + INTERLINE_SPACE);
        g.dispose();

    }


    /**
     * Metodo encargado de gestionar el movimiento de los sprites y sus colisiones, además de calcular el
     * cooldown del laser
     */
    public void manageGraphics() {
        if (isGaming) {
            for (Sprite s : sprites) {
                moveSprites(s);
                checkWallsCollision(s);
                checkAsteroidShooted();
            }
            if (shootCooldown) {
                checkCoolDown();
            }
            destroyCollisionedSprites();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                sleep(20);
                manageGraphics();
                Toolkit.getDefaultToolkit().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //no hace nada
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if (isGaming) {
            spaceShip.setPosX(e.getX() - spaceShip.getAncho() / 2);
            spaceShip.setPosY(e.getY() - spaceShip.getAlto() / 2);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isGaming) {
            addAsteroids();
            addSpaceShip();
            addTimer();

            isGaming = true;
        }

    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (!shootCooldown && isGaming) {
            laserShoot = new Sprite();
            laserShoot.setAncho(WIDTH_SHOOT);
            laserShoot.setAlto(HEIGHT_SHOOT);
            laserShoot.setPosX(e.getX() - spaceShip.getAncho() / 2);
            laserShoot.setPosY(e.getY() - spaceShip.getAlto() / 2);
            laserShoot.setVy(VELOCITY_SHOOT);
            laserShoot.setFileImage(new File(LASER_IMAGE));
            laserShoot.refreshBuffer();
            this.shootCooldown = true;
            sprites.add(laserShoot);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

