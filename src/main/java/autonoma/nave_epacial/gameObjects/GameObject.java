package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La clase abstracta GameObject representa la base para todos los objetos
 * interactivos y visuales dentro del juego NaveEspacial.
 * Define las propiedades fundamentales como la posición y la textura,
 * además de establecer el contrato para la actualización de lógica y renderizado.
 * * @author Gemini
 * @version 1.0
 */
public abstract class GameObject {
    /** La apariencia visual del objeto representada por una imagen. */
    protected BufferedImage texture;
    /** La ubicación actual del objeto en el espacio bidimensional del juego. */
    protected Vector2D position;

    /**
     * Crea una nueva instancia de GameObject.
     * * @param position Coordenadas iniciales del objeto.
     * @param texture Imagen que se utilizará para representar el objeto.
     */
    public GameObject(Vector2D position, BufferedImage texture) {
        this.position = position;
        this.texture = texture;
    }

    /**
     * Actualiza la lógica interna del objeto (movimiento, colisiones, estados).
     * Este método debe ser implementado por las clases hijas según su comportamiento.
     */
    public abstract void update();

    /**
     * Dibuja el objeto en la pantalla utilizando el contexto gráfico proporcionado.
     * * @param var1 El contexto gráfico sobre el cual se renderizará la textura del objeto.
     */
    public abstract void draw(Graphics var1);

    /**
     * Obtiene la posición actual del objeto.
     * * @return Un objeto {@link Vector2D} con las coordenadas actuales.
     */
    public Vector2D getPosition() {
        return this.position;
    }

    /**
     * Establece una nueva posición para el objeto en el mundo del juego.
     * * @param position La nueva posición de tipo {@link Vector2D}.
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }
}