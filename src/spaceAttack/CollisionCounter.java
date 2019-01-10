package spaceAttack;

import javax.swing.*;
import java.awt.*;

public class CollisionCounter extends JLabel {

    int collisions;

    public CollisionCounter(){
        this.collisions = 0;

        Font font = new Font("MonoSpaced",Font.BOLD,32);

        this.setForeground(Color.RED);
        this.setHorizontalAlignment(CENTER);

        refreshCounter();

    }

    public void refreshCounter(){
        this.setText(String.valueOf(collisions));
    }

    public void plusCollision(){
        this.collisions+=1;
    }



}
