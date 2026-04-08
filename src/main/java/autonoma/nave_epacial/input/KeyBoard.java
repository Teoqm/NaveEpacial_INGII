package autonoma.nave_epacial.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener {

	private boolean[] keys = new boolean[256];

	// Jugador 1 — flechas + P
	public static boolean UP, LEFT, RIGHT, SHOOT;

	// Jugador 2 — WASD + R
	public static boolean UP2, LEFT2, RIGHT2, SHOOT2;

	public void update() {
		UP    = keys[KeyEvent.VK_UP];
		LEFT  = keys[KeyEvent.VK_LEFT];
		RIGHT = keys[KeyEvent.VK_RIGHT];
		SHOOT = keys[KeyEvent.VK_P];

		UP2    = keys[KeyEvent.VK_W];
		LEFT2  = keys[KeyEvent.VK_A];
		RIGHT2 = keys[KeyEvent.VK_D];
		SHOOT2 = keys[KeyEvent.VK_R];
	}

	@Override public void keyPressed(KeyEvent e)  { keys[e.getKeyCode()] = true;  }
	@Override public void keyReleased(KeyEvent e) { keys[e.getKeyCode()] = false; }
	@Override public void keyTyped(KeyEvent e)    {}
}