package autonoma.nave_epacial.graphics;

import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;

/**
 * La clase Text proporciona métodos de utilidad estáticos para renderizar texto
 * en la pantalla de forma simplificada.
 * Permite gestionar el color, la fuente y el alineamiento (centrado o por coordenadas directas)
 * calculando automáticamente las métricas de la fuente necesarias.
 * * @author Gemini
 * @version 1.0
 */
public class Text {
    /**
     * Dibuja una cadena de texto en el contexto gráfico proporcionado.
     * * @param g      El contexto gráfico sobre el cual se dibujará el texto.
     * @param text   La cadena de caracteres a mostrar.
     * @param pos    La posición {@link Vector2D} base para el dibujo.
     * @param center Si es {@code true}, el texto se centrará horizontal y verticalmente
     * respecto a la posición dada; si es {@code false}, se usará como
     * punto de origen (línea base).
     * @param color  El color con el que se renderizará el texto.
     * @param font   La fuente tipográfica {@link Font} que se aplicará al texto.
     */
    public static void drawText(Graphics g, String text, Vector2D pos, boolean center, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        Vector2D position = new Vector2D(pos.getX(), pos.getY());

        if(center) {
            FontMetrics fm = g.getFontMetrics();
            position.setX(position.getX() - fm.stringWidth(text) / 2);
            position.setY(position.getY() - fm.getHeight() / 2);

        }

        g.drawString(text, (int)position.getX(), (int)position.getY());

    }
}