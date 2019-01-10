package spaceAttack;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author David Bermejo Simon
 **/
public class Sprite {
    private BufferedImage buffer;
    private Color color;

    //variables de dimension
    private int ancho;
    private int alto;

    //variables de colocacion
    private int posX;
    private int posY;

    private int vx;
    private int vy;

    private int idSprite;

    private boolean destroyed;

    private File fileImage;

    public Sprite() {
        this.destroyed=false;

    }

    public Sprite(BufferedImage buffer, Color color, int ancho, int alto, int posX, int posY) {
        this.buffer = buffer;
        this.color = color;
        this.ancho = ancho;
        this.alto = alto;
        this.posX = posX;
        this.posY = posY;
        this.destroyed=false;
//        refreshBuffer();
    }

    public Sprite(BufferedImage buffer, Color color, int ancho, int alto, int posX, int posY, File fileImage) {
        this.buffer = buffer;
        this.color = color;
        this.ancho = ancho;
        this.alto = alto;
        this.posX = posX;
        this.posY = posY;
        this.fileImage = fileImage;


    }

    public void refreshBuffer() {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffer.getGraphics();
        try{
            BufferedImage imagenSprite = ImageIO.read(fileImage);
            g.drawImage(imagenSprite.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH),0,0,null);
            g.dispose();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
            g.setColor(color);
            g.fillRect(0, 0, ancho, alto);
            g.dispose();
        }
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getIdSprite() {
        return idSprite;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setIdSprite(int idSprite) {
        this.idSprite = idSprite;
    }

    public File getFileImage() {
        return fileImage;
    }

    public void setFileImage(File fileImage) {
        this.fileImage = fileImage;
    }

    /**
     * Metodo encargado de determinar si un sprite colisiona con otro
     * @param s2 : sprite a comparar
     * @return check : true si colisionan, false si no.
     */
    public boolean isCollides(Sprite s2) {

        boolean collidesX =false , collidesY = false;

        //calculo de la colision en el eje horizontal
        if(this.getPosX()<s2.getPosX()){
            int rightBorder = this.getPosX()+this.getAncho();
            if(rightBorder>=s2.getPosX()){
                collidesX=true;
            }
        }else{
            int rightBorder = s2.getPosX()+s2.getAncho();
            if(rightBorder>= this.getPosX()){
                collidesX=true;
            }
        }

        //calculo de la colision en el eje vertical
        if(this.getPosY()<s2.getPosY()){
            int bottomBorder = this.getPosY()+this.getAlto();
            if(bottomBorder>= s2.getPosY()){
                collidesY=true;
            }
        }else{
            int bottomBorder = s2.getPosY()+s2.getAlto();
            if(bottomBorder>=this.getPosY()){
                collidesY=true;
            }
        }

        return collidesX && collidesY;
    }

    /**
     * Metodo encargado de cambiar la velocidad en el vector contrario.
     */
    public void changeVelocity(){
        if(this.getVx() < 0){
            this.setVx(Math.abs(this.getVx()));
        }else{
            this.setVx(Math.abs(this.getVx())*-1);
        }

        if(this.getVx()>0){
            this.setVy(Math.abs(this.getVy()));
        }else{
            this.setVy(Math.abs(this.getVy())*-1);
        }
    }
}
