package autonoma.nave_epacial.gameObjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

/**
 * La clase Message representa un texto flotante con efectos de transparencia (fade)
 * que se utiliza para mostrar notificaciones, puntajes o mensajes temporales en el juego.
 * Los mensajes se desplazan hacia arriba automáticamente y se destruyen al desvanecerse.
 * * @author Gemini
 * @version 1.0
 */
public class Message {
    /** Nivel de transparencia actual del mensaje (0.0 a 1.0). */
    private float alpha;
    /** Contenido de texto a mostrar. */
    private String text;
    /** Ubicación actual del mensaje en la pantalla. */
    private Vector2D position;
    /** Color del texto. */
    private Color color;
    /** Indica si el texto debe dibujarse centrado respecto a la posición. */
    private boolean center;
    /** * Indica el modo de desvanecimiento:
     * true para desaparecer (fade out), false para aparecer (fade in).
     */
    private boolean fade;
    /** Fuente tipográfica utilizada para el renderizado. */
    private Font font;
    /** Tasa de cambio de la transparencia en cada frame. */
    private final float deltaAlpha = 0.01f;
    /** Indica si el mensaje ha terminado su ciclo de vida y debe ser removido. */
    private boolean dead;

    /**
     * Construye un nuevo mensaje con propiedades visuales y de comportamiento específicas.
     * * @param position Coordenadas iniciales del mensaje.
     * @param fade     Si es true, el mensaje empieza visible y se desvanece;
     * si es false, aparece gradualmente.
     * @param text     El contenido del mensaje.
     * @param color    Color del texto.
     * @param center   Determina si la posición es el centro del texto.
     * @param font     Fuente a utilizar.
     */
    public Message(Vector2D position, boolean fade, String text, Color color,
                   boolean center, Font font) {
        this.font = font;
        this.text = text;
        this.position = position;
        this.fade = fade;
        this.color = color;
        this.center = center;
        this.dead = false;

        if (fade)
            alpha = 1;
        else
            alpha = 0;

    }

    /**
     * Renderiza el mensaje en el contexto gráfico, actualiza su transparencia
     * y lo desplaza hacia arriba. Controla el ciclo de vida del mensaje (aparición/desaparición).
     * * @param g2d El contexto gráfico 2D para realizar el dibujo con transparencias.
     */
    public void draw(Graphics2D g2d) {

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Text.drawText(g2d, text, position, center, color, font);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        position.setY(position.getY() - 1);

        if (fade)
            alpha -= deltaAlpha;
        else
            alpha += deltaAlpha;

        if (fade && alpha < 0) {
            dead = true;
        }

        if (!fade && alpha > 1) {
            fade = true;
            alpha = 1;
        }

    }

    /**
     * Informa si el mensaje ha completado su animación de desvanecimiento.
     * * @return {@code true} si el mensaje debe ser eliminado del juego, {@code false} de lo contrario.
     */
    public boolean isDead() {
        return dead;
    }

}