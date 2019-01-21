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

    private String idSprite;

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

    public String getIdSprite() {
        return idSprite;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setIdSprite(String idSprite) {
        this.idSprite = idSprite;
    }

    public void setFileImage(File fileImage) {
        this.fileImage = fileImage;
    }





    /**
     * Metodo encargado de crear un collider cuadrado y determinar si un sprite
     * colisiona con otro
     * @param s2 : sprite a comparar
     * @return check : true si colisionan, false si no.
     */
    public boolean squareCollider(Sprite s2){
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
     * Metodo encargado de crear un collider circular y determinar si un sprite
     * colisiona con otro
     * @return check : true si colisionan, false si no.
     */
//    public boolean circleCollider(Sprite s2) {
//        //valor entero para representar la distancia
//        double d;
//        int radioS1=this.getAncho()/2;
//        int radioS2=s2.getAncho()/2;
//
//        //valor entero para representar la suma de los radios
//        int plusRadios = radioS1 + radioS2;
//        boolean collides;
//
//        //vector de 2 dimensiones con el valor x e y del centro del sprite original.
//        int[] center1 = {this.getPosX() - radioS1, this.getPosY() - radioS1};
//        //vector de 2 dimensiones con el valor x e y del centro del sprite s2.
//        int[] center2 = {s2.getPosX() - radioS2, s2.getPosY() - radioS2};
//
//        if (checkCenterCloseness(center1, center2) == 0) {
//            d = Math.sqrt(Math.pow(center2[0]-center1[0], 2) + Math.pow(center2[1]-center1[1], 2));
//        } else {
//            d = Math.sqrt(Math.pow(center1[0]-center2[0], 2) + Math.pow(center1[1]-center2[1], 2));
//        }
//
//        if (d <= plusRadios) {
//            collides = true;
//        } else {
//            collides = false;
//        }
//        return collides;
//    }

    /**
     * Metodo encargado de comprobar cual de los puntos es más cercano al eje de coordenadas 0,0
     *
     * @param center1
     * @param center2
     * @return 0 si el centro 1 es el mas cercano, 1 en caso contrario.
     */
//    private int checkCenterCloseness(int[] center1, int[] center2) {
//        double d1, d2;
//        int result;
//        d1 = Math.sqrt(Math.pow(center1[0], 2) + Math.pow(center1[1], 2));
//        d2 = Math.sqrt(Math.pow(center2[0], 2) + Math.pow(center2[1], 2));
//        if (d1 <= d2) {
//            result = 0;
//        } else {
//            result = 1;
//        }
//        return result;
//    }



    /**
     * Metodo encargado de cambiar la velocidad en el vector contrario.
     */
//    public void changeVelocity(){
//        if(this.getVx() < 0){
//            this.setVx(Math.abs(this.getVx()));
//        }else{
//            this.setVx(Math.abs(this.getVx())*-1);
//        }
//
//        if(this.getVx()>0){
//            this.setVy(Math.abs(this.getVy()));
//        }else{
//            this.setVy(Math.abs(this.getVy())*-1);
//        }
//    }
}
