package autonoma.nave_epacial.states;

import java.awt.*;
import java.io.IOException;

/**
 * Clase abstracta que representa un estado dentro del juego.
 *
 * Permite manejar diferentes estados (como menú, juego, pausa, etc.)
 * mediante un patrón de diseño basado en estados.
 *
 * También gestiona el estado actual de forma global.
 *
 * @author Mateo Quintero
 * @author Juan Jacobo Cañas
 * @author Juan Hernández
 * @author Juan José Morales
 *  * * @version 1.0
 */
public abstract class State {

    /**
     * Estado actual activo en el juego.
     */
    private static State currentState = null;

    /**
     * Obtiene el estado actual.
     *
     * @return el estado actual del juego
     */
    public static State getCurrentState() {return currentState;}

    /**
     * Cambia el estado actual del juego.
     *
     * @param newState el nuevo estado a establecer
     */
    public static void changeState(State newState) {
        currentState = newState;
    }

    /**
     * Actualiza la lógica del estado.
     *
     * @throws IOException si ocurre un error de entrada/salida durante la actualización
     */
    public abstract void update() throws IOException;

    /**
     * Dibuja los elementos gráficos del estado.
     *
     * @param g objeto Graphics utilizado para renderizar en pantalla
     */
    public abstract void draw(Graphics g);

}