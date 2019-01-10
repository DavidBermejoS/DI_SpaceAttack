package spaceAttack;


import java.awt.*;

/**
 * @author David Bermejo Simon
 **/
public class Main {

    /**
     * Hilo principal "main"
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Window window = new Window();
                window.inicializate();
            }
        });
    }
}
