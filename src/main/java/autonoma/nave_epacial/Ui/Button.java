package autonoma.nave_epacial.Ui;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.input.MouseInput;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase que representa un botón dentro de la interfaz gráfica del juego.
 *
 * El botón cambia su apariencia cuando el cursor del mouse está sobre él
 * y ejecuta una acción cuando se hace clic.
 *
 * Utiliza imágenes para representar los estados "normal" y "hover",
 * además de un área de colisión para detectar interacción.
 *
 * @author Mateo Quintero
 * @author Juan Jacobo Cañas
 * @author Juan Hernández
 * @author Juan José Morales
 *  * * @version 1.0
 */
public class Button {

    /**
     * Imagen del botón cuando el mouse no está sobre él.
     */
    private BufferedImage mouseOutImg;

    /**
     * Imagen del botón cuando el mouse está sobre él.
     */
    private BufferedImage mouseInImg;

    /**
     * Indica si el mouse está actualmente sobre el botón.
     */
    private boolean mouseIn;

    /**
     * Área que define los límites del botón para detectar interacción.
     */
    private Rectangle boundingBox;

    /**
     * Acción que se ejecuta al hacer clic en el botón.
     */
    private Action action;

    /**
     * Texto que se muestra en el botón.
     */
    private String text;

    /**
     * Constructor del botón.
     *
     * Inicializa las imágenes del botón, su posición, el texto
     * y la acción asociada.
     *
     * @param mouseOutImg imagen cuando el mouse no está sobre el botón
     * @param mouseInImg imagen cuando el mouse está sobre el botón
     * @param x posición horizontal del botón
     * @param y posición vertical del botón
     * @param text texto que se mostrará en el botón
     * @param action acción que se ejecutará al hacer clic
     */
    public Button(
            BufferedImage mouseOutImg,
            BufferedImage mouseInImg,
            int x, int y,
            String text,
            Action action
    ) {
        this.mouseInImg = mouseInImg;
        this.mouseOutImg = mouseOutImg;
        this.text = text;
        boundingBox = new Rectangle(x, y, mouseInImg.getWidth(), mouseInImg.getHeight());
        this.action = action;
    }

    /**
     * Actualiza el estado del botón.
     *
     * Detecta si el cursor está sobre el botón y si se ha realizado
     * un clic, en cuyo caso ejecuta la acción asociada.
     */
    public void update() {

        if(boundingBox.contains(MouseInput.X, MouseInput.Y)) {
            mouseIn = true;
        }else {
            mouseIn = false;
        }

        if(mouseIn && MouseInput.MLB) {
            action.doAction();
        }
    }

    /**
     * Dibuja el botón en pantalla.
     *
     * Renderiza la imagen correspondiente dependiendo de si el mouse
     * está sobre el botón y muestra el texto centrado.
     *
     * @param g objeto Graphics utilizado para dibujar en pantalla
     */
    public void draw(Graphics g) {

        if(mouseIn) {
            g.drawImage(mouseInImg, boundingBox.x, boundingBox.y, null);
        }else {
            g.drawImage(mouseOutImg, boundingBox.x, boundingBox.y, null);
        }

        Text.drawText(
                g,
                text,
                new Vector2D(
                        boundingBox.getX() + boundingBox.getWidth() / 2,
                        boundingBox.getY() + boundingBox.getHeight()),
                true,
                Color.BLACK,
                Assets.fontMed);


    }

}