package screens;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author David Bermejo Simon
 **/
public interface Screen {

    public void startScreen();

    public void drawScreen(Graphics g) throws InterruptedException;

    public void startFrame();

    public void moveMouse(MouseEvent e);

    public void clickMouse(MouseEvent e);

    public void resizeScreen(Graphics g);

    public Graphics getGraphics();

    public void keyPressed(KeyEvent e);

    public void manageGameFunctions() throws InterruptedException;
}
