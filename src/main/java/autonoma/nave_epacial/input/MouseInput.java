package autonoma.nave_epacial.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * La clase MouseInput extiende de {@link MouseAdapter} para gestionar la interacción
 * del usuario mediante el ratón.
 * Proporciona acceso estático a las coordenadas actuales del puntero y al estado
 * del botón principal, facilitando la detección de clics en menús y otros
 * elementos de la interfaz de usuario.
 * * @version 1.0
 */
public class MouseInput extends MouseAdapter {

    /** Coordenada X actual del puntero del ratón en la ventana. */
    public static int X;
    /** Coordenada Y actual del puntero del ratón en la ventana. */
    public static int Y;
    /** * Indica si el botón izquierdo del ratón (Mouse Left Button)
     * se encuentra presionado actualmente.
     */
    public static boolean MLB;

    /**
     * Se activa cuando se presiona un botón del ratón.
     * Actualiza {@code MLB} a {@code true} si el botón presionado es el principal.
     * * @param e El evento de ratón que contiene la información del clic.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            MLB = true;
        }
    }

    /**
     * Se activa cuando se libera un botón del ratón.
     * Restablece {@code MLB} a {@code false} si el botón liberado es el principal.
     * * @param e El evento de ratón que contiene la información de la liberación.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            MLB = false;
        }
    }

    /**
     * Se activa cuando el ratón es arrastrado con un botón presionado.
     * Actualiza las coordenadas {@code X} e {@code Y} del puntero.
     * * @param e El evento de ratón que contiene las coordenadas actuales.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

    /**
     * Se activa cuando el ratón se mueve sin necesidad de presionar botones.
     * Actualiza las coordenadas {@code X} e {@code Y} del puntero.
     * * @param e El evento de ratón que contiene las coordenadas actuales.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

}