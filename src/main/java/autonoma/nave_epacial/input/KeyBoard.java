package autonoma.nave_epacial.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * La clase KeyBoard implementa la interfaz {@link KeyListener} para gestionar las entradas de teclado.
 * Proporciona un sistema de estados para las teclas, permitiendo diferenciar los controles
 * de dos jugadores simultáneos mediante esquemas de teclas distintos (Flechas/P y WASD/R).
 * * @version 1.0
 */
public class KeyBoard implements KeyListener {

	/** Arreglo que almacena el estado presionado (true) o liberado (false) de cada tecla. */
	private boolean[] keys = new boolean[256];

	/** Estados de control para el Jugador 1 (Arriba, Izquierda, Derecha, Disparar). */
	public static boolean UP, LEFT, RIGHT, SHOOT;

	/** Estados de control para el Jugador 2 (Arriba, Izquierda, Derecha, Disparar). */
	public static boolean UP2, LEFT2, RIGHT2, SHOOT2;

	/**
	 * Actualiza las variables estáticas de control basándose en el estado actual del arreglo de teclas.
	 * Este método debe ser llamado en cada iteración del Game Loop para sincronizar la lógica con la entrada.
	 */
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

	/**
	 * Se activa cuando una tecla es presionada. Actualiza el arreglo interno a {@code true}.
	 * @param e El evento de teclado.
	 */
	@Override public void keyPressed(KeyEvent e)  {
		if(e.getKeyCode() < keys.length)
			keys[e.getKeyCode()] = true;
	}

	/**
	 * Se activa cuando una tecla es liberada. Actualiza el arreglo interno a {@code false}.
	 * @param e El evento de teclado.
	 */
	@Override public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < keys.length)
			keys[e.getKeyCode()] = false;
	}

	/**
	 * No se utiliza en esta implementación.
	 * @param e El evento de teclado.
	 */
	@Override public void keyTyped(KeyEvent e)    {}
}