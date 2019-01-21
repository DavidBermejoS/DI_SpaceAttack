package screens;

import spaceAttack.GamePane;
import spaceAttack.Sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author David Bermejo Simon
 **/
public class FirstScreen extends JPanel implements Screen {

    private static final int WIDTH_SHOOT = 20;
    private static final int HEIGHT_SHOOT = 40;
    private static final int VELOCITY_SHOOT = -30;

    private  static  final  int NUM_ASTEROID = 3;
    private static final int WIDTH_ASTEROID = 40;
    private static final int HEIGHT_ASTEROID = 40;

    private static final int WIDTH_SPACESHIP = 50;
    private static final int HEIGHT_SPACESHIP = 70;


    private static final String ASTEROID_IMAGE = "resources/images/asteroide.png";
    private static final String LASER_IMAGE = "resources/images/laser.png";
    private static final String BACKGROUND_GAME = "resources/images/background.jpg";

    private int numSprites;
    private double timeCount;
    private boolean shootCooldown;

    ArrayList<Sprite> sprites;
    Sprite spaceShip;
    Sprite laserShoot;
    Timer timer;
    Image backgroundImage;

    Screen actualScreen;
    GamePane gamePane;
    String spaceShipImageRoute;
    int targetsDestroyed;
    boolean playersAlive;

    public FirstScreen(GamePane gamePane, String SpaceShip_Image) {
        this.spaceShipImageRoute = SpaceShip_Image;
        actualScreen = this;
        this.gamePane = gamePane;
        startFrame();
    }

    //INSTANCIACION DE PARAMETROS DE LA PARTIDA E INICIO
    @Override
    public void startFrame() {
        this.numSprites = 0;
        this.targetsDestroyed = 0;
        this.playersAlive = true;
        sprites = new ArrayList<>();
        startScreen();

    }


    //INSTANCIACION DE ELEMENTOS DEL JUEGO
    @Override
    public void startScreen() {
        addAsteroids();
        addSpaceShip();
        addTimer();
    }

    /**
     * Metodo encargado de anadir los asteroides al inicio de la partida
     */
    private void addAsteroids() {
        for (int i = 0; i < NUM_ASTEROID; i++) {
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
            sprite.setIdSprite("asteroid");
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
        spaceShip.setPosX(gamePane.getWidth());
        spaceShip.setPosY(gamePane.getHeight());
        spaceShip.setFileImage(new File(spaceShipImageRoute));
        spaceShip.refreshBuffer();
        spaceShip.setIdSprite("spaceship");
        sprites.add(spaceShip);
        numSprites++;

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

    //GESTION DE LOS GRÁFICOS DEL JUEGO

    @Override
    public void drawScreen(Graphics g) throws InterruptedException {
        drawBackground(g);
        drawSprite(g);
        drawTimer(g);
        manageGameFunctions();
    }

    /**
     * Metodo encargado de redimensionar la imagen de fondo según el tamaño de la ventana
     *
     * @param g
     */
    @Override
    public void resizeScreen(Graphics g) {
        backgroundImage = backgroundImage.getScaledInstance(gamePane.getWidth(), gamePane.getHeight(), 4);
    }

    /**
     * Metodo encargado de pintar el fondo del panel de juego
     *
     * @param g
     */
    private void drawBackground(Graphics g) {
        File bckg = new File(BACKGROUND_GAME);
        try {
            backgroundImage = ImageIO.read(bckg);
            resizeScreen(g);
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen de fondo");
            System.out.println("Error: " + e.getMessage());
        }
        g.drawImage(backgroundImage, 0, 0, null);
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


    //METODOS DE GESTIÓN DEL SISTEMA DE JUEGO.
    /**
     * Metodo encargado de gestionar el movimiento de los sprites y sus colisiones, además de calcular el
     * cooldown del laser
     */
    public void manageGameFunctions() throws InterruptedException {
        for (Sprite s : sprites) {
            moveSprites(s);
            checkWallsCollision(s);
            checkAsteroidShooted();
        }
        if (shootCooldown) {
            checkCoolDown();
        }
        destroyCollisionedSprites();
        checkEndGame();
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
     * Metodo encargado de comprobar las colisiones con las paredes de la ventana y cambiar la velocidad
     * en caso de que exista dicha colision
     *
     * @param sprite
     */
    private void checkWallsCollision(Sprite sprite) {
        if (sprite != laserShoot) {
            if (sprite.getPosX() <= 0) {
                sprite.setVx(Math.abs(sprite.getVx()));
            } else if (sprite.getPosX() >= gamePane.getWidth() - sprite.getAncho()) {
                sprite.setVx(Math.abs(sprite.getVx()) * -1);
            }

            if (sprite.getPosY() <= 0) {
                sprite.setVy(Math.abs(sprite.getVy()));
            } else if (sprite.getPosY() >= gamePane.getHeight() - sprite.getAlto()) {
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
        }else{
            for (Sprite s:sprites) {
                if(s!=spaceShip && s!=laserShoot){
                    if(s.squareCollider(spaceShip)){
                        s.setDestroyed(true);
                        spaceShip.setDestroyed(true);
                        playersAlive = false;

                    }
                }

            }
        }
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
     * Metodo encargado de crear una copia de la lista de sprites. En esta copia se eliminarán los sprites colisionados.
     * Posteriormente la lista original se actualizará con la información de la copia.
     */
    private void destroyCollisionedSprites() {
        ArrayList<Sprite> spritesAux = (ArrayList<Sprite>) sprites.clone();
        for (Sprite s : sprites) {
            if (s.isDestroyed()) {
                spritesAux.remove(s);
                if (s.getIdSprite().equalsIgnoreCase("asteroid")){
                    targetsDestroyed++;
                    System.out.println("Objetivos destruidos: "+targetsDestroyed);
                }
            }
        }

            sprites = (ArrayList<Sprite>) spritesAux.clone();
    }

    /**
     * Metodo encargado de gestionar el final del juego, es decir, marcar en que situaciones
     * se marcará como fin de juego o como fin del nivel
     */
    private void checkEndGame() {
        if((targetsDestroyed == NUM_ASTEROID) && playersAlive){
            this.gamePane.setGameOver(false);
            this.gamePane.setEndLevel(true);

        }else if(!playersAlive){
            this.gamePane.setGameOver(true);
            this.gamePane.setEndLevel(true);
        }
    }

    /**
     * Metodo Get del objeto screen para trabajar desde GamePane
     *
     * @return Graphics g : componente graphics de la clase
     */
    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //no hace nada
    }


    //EVENTOS DE RATON

    @Override
    public void moveMouse(MouseEvent e) {
        spaceShip.setPosX(e.getX() - spaceShip.getAncho() / 2);
        spaceShip.setPosY(e.getY() - spaceShip.getAlto() / 2);
    }

    @Override
    public void clickMouse(MouseEvent e) {
        if (!shootCooldown) {
            laserShoot = new Sprite();
            laserShoot.setAncho(WIDTH_SHOOT);
            laserShoot.setAlto(HEIGHT_SHOOT);
            laserShoot.setPosX(e.getX() - spaceShip.getAncho() / 2);
            laserShoot.setPosY(e.getY() - spaceShip.getAlto() / 2);
            laserShoot.setVy(VELOCITY_SHOOT);
            laserShoot.setFileImage(new File(LASER_IMAGE));
            laserShoot.setIdSprite("lasershoot");
            laserShoot.refreshBuffer();
            this.shootCooldown = true;
            sprites.add(laserShoot);
        }
    }


}
