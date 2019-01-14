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
 * @author David Bermejo Simon
 **/
public class GamePane extends JPanel implements Runnable, MouseMotionListener, MouseListener {

    private static final int WIDTH_SHOOT = 20;
    private static final int HEIGHT_SHOOT = 40;
    private static final int WIDTH_ASTEROID = 40;
    private static final int HEIGHT_ASTEROID = 40;
    private static final int WIDTH_SPACESHIP = 30;
    private static final int HEIGHT_SPACESHIP = 40;
    private static final int VELOCITY_SHOOT = -20;

    ArrayList<Sprite> sprites;

    Sprite spaceShip;
    Sprite laserShoot;
    Timer timer;

    int numSprites;

    //imagen de fondo
    Image backgroundImage;

    //rutas relativas a los recursos de imagen
    String asteroidImage = "resources/images/asteroide.png";
    String spaceShipImage = "resources/images/nave.png";
    String laserImage = "resources/images/laser.png";


    double timeCount;

    //variable lógica para comprobar que la nave ya ha disparado
    boolean shootCooldown;

    public GamePane() {
        this.numSprites = 0;
        sprites = new ArrayList<>();
        //se anaden los asteroides, la nave y el tiempo
        addAsteroids();
        addSpaceShip();
        addTimer();
        //se inicializa el tiempo
        this.timer.start();
        //comienza el ciclo de refresco del panel de juego con sus listeners
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
            sprite.setFileImage(new File(asteroidImage));
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
        spaceShip.setFileImage(new File(spaceShipImage));
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
    }


    /**
     * Metodo para repintar los componentes en la pantalla
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        drawBackground(g);
        drawSprite(g);
//        drawShoot(g);
        drawTimer(g);
    }

//    private void drawShoot(Graphics g) {
//        if (laserShoot != null) {
//            g.drawImage(laserShoot.getBuffer(),laserShoot.getPosX(),laserShoot.getPosY(),null);
//        }
//    }


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
        if(shootCooldown){
            for (int i = 0; i < sprites.size(); i++) {
                Sprite s1 = sprites.get(i);
                if(s1!=spaceShip && s1!=laserShoot){
                    if (s1.squareCollider(laserShoot)){
                        s1.setDestroyed(true);
                        laserShoot.setDestroyed(true);
                        this.shootCooldown=false;
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
//            laserShoot.setColor(null);
            sprites.remove(laserShoot);
            this.shootCooldown = false;
        }
    }

    public void moveSprites(Sprite s){
        s.setPosX(s.getPosX() + s.getVx());
        s.setPosY(s.getPosY() + s.getVy());
    }


    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                sleep(20);

                for (Sprite s : sprites) {
                    moveSprites(s);
                    checkWallsCollision(s);
                    checkAsteroidShooted();
                }
                if (shootCooldown) {
                    checkCoolDown();
                }
                destroyCollisionedSprites();


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
        spaceShip.setPosX(e.getX() - spaceShip.getAncho() / 2);
        spaceShip.setPosY(e.getY() - spaceShip.getAlto() / 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!shootCooldown) {
            laserShoot = new Sprite();
            laserShoot.setAncho(WIDTH_SHOOT);
            laserShoot.setAlto(HEIGHT_SHOOT);
            laserShoot.setPosX(e.getX() - spaceShip.getAncho() / 2);
            laserShoot.setPosY(e.getY() - spaceShip.getAlto() / 2);
            laserShoot.setVy(VELOCITY_SHOOT);
            laserShoot.setFileImage(new File(laserImage));
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

