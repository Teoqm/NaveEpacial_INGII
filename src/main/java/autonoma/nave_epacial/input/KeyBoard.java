package autonoma.nave_epacial.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener {

    private boolean[] keys = new boolean[256];

    public static Boolean UP, LEFT, RIGHT;
    /* clase que ayuda mover la clase con los metdos key de l impemntacion de keyListener*/
    public KeyBoard(){

        UP = false;
        LEFT = false;
        RIGHT = false;

    }

    public void update(){

        UP = keys[KeyEvent.VK_UP];
        LEFT = keys[KeyEvent.VK_LEFT];
        RIGHT = keys[KeyEvent.VK_RIGHT];
    }

    @Override
    public void keyTyped(KeyEvent e) {

        keys[e.getKeyCode()] = true;
        System.out.println(e.getKeyChar());

    }

    @Override
    public void keyReleased(KeyEvent e) {

        keys[e.getKeyCode()] = false;

    }


    @Override
    public void keyPressed(KeyEvent e) {

    }


}
