package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;

import java.awt.image.BufferedImage;

/**
 * El enumerador Size define las dimensiones y la jerarquía de fragmentación
 * de los meteoros en el juego.
 * Cada tamaño especifica cuántos fragmentos se generarán al ser destruido
 * y qué texturas deben utilizar los fragmentos resultantes.
 * @version 1.0
 */
public enum Size {
    /** Tamaño grande: al destruirse, genera 2 fragmentos de tamaño MED. */
    BIG(2, Assets.meds),
    /** Tamaño mediano: al destruirse, genera 2 fragmentos de tamaño SMALL. */
    MED(2, Assets.smalls),
    /** Tamaño pequeño: al destruirse, genera 2 fragmentos de tamaño TINY. */
    SMALL(2, Assets.tinies),
    /** Tamaño diminuto: último nivel de la jerarquía, no genera más fragmentos. */
    TINY(0, null);

    /** * Cantidad de fragmentos que se crean cuando un objeto de este
     * tamaño es destruido.
     */
    public int quantity;

    /** * Arreglo de imágenes disponibles para los fragmentos resultantes
     * de la destrucción de este tamaño.
     */
    public BufferedImage[] textures;

    /**
     * Constructor del enumerador Size.
     * * @param quantity Número de piezas en las que se divide.
     * @param textures Conjunto de texturas para los hijos resultantes.
     */
    private Size(int quantity, BufferedImage[] textures) {
        this.quantity = quantity;
        this.textures = textures;
    }
}