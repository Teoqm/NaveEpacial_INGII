package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.UdpClient;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * La clase Meteor representa los obstáculos principales del juego.
 * Estos objetos se desplazan por la pantalla con un movimiento constante y rotación,
 * poseen la capacidad de dividirse en fragmentos más pequeños al ser destruidos
 * y pueden transferirse entre diferentes instancias de juego mediante red UDP.
 * * @author Gemini
 * @version 1.0
 */
public class Meteor extends MovingObject {

    /** El tamaño actual del meteoro (Grande, Mediano o Pequeño). */
    private final Size      size;
    /** Cliente UDP utilizado para sincronizar el cruce del meteoro entre pantallas. */
    private UdpClient udpClient;

    /**
     * Construye un nuevo Meteor con dimensiones, velocidad y tamaño específicos.
     * * @param position  Coordenadas iniciales en el espacio de juego.
     * @param velocity  Vector de dirección del movimiento.
     * @param maxVel    Velocidad máxima permitida para este meteoro.
     * @param texture   Imagen visual del meteoro.
     * @param gameState Referencia al estado del juego para gestionar colisiones y puntajes.
     * @param size      Categoría de tamaño del meteoro.
     */
    public Meteor(Vector2D position, Vector2D velocity, double maxVel,
                  BufferedImage texture, GameState gameState, Size size) {
        super(position, velocity, maxVel, texture, gameState);
        this.size     = size;
        this.velocity = velocity.scale(maxVel);
    }

    /**
     * Establece el cliente UDP para la comunicación entre pares.
     * * @param client Instancia de {@link UdpClient}.
     */
    public void setUdpClient(UdpClient client) { this.udpClient = client; }

    /**
     * Actualiza la posición y rotación del meteoro.
     * Gestiona el cruce de límites horizontales para transferencia de red y
     * el efecto de "envoltura" (wrap-around) en los límites verticales.
     */
    @Override
    public void update() {
        position = position.add(velocity);

        if (position.getX() > Constants.WIDTH) {
            sendCross("RIGHT");
            destroyQuiet();
            return;
        }
        if (position.getX() < -width) {
            sendCross("LEFT");
            destroyQuiet();
            return;
        }

        if (position.getY() > Constants.HEIGHT) position.setY(-height);
        if (position.getY() < -height)          position.setY(Constants.HEIGHT);

        angle += Constants.DELTAANGLE / 2;

        // FIX: verificar colisiones con jugadores y lasers
        collidesWith();
    }

    /**
     * Notifica a través de la red que el meteoro ha salido de la pantalla
     * local para aparecer en la del equipo remoto.
     * * @param side Lado por el cual salió el objeto ("LEFT" o "RIGHT").
     */
    private void sendCross(String side) {
        if (udpClient == null) return;
        udpClient.send(new GameMessage(MessageType.METEOR_CROSS,
                side,
                String.valueOf(position.getY()),
                String.valueOf(velocity.getX()),
                String.valueOf(velocity.getY()),
                String.valueOf(maxVel),
                size.name()));
    }

    /** * Realiza una eliminación del objeto sin activar eventos de juego.
     * Se utiliza cuando el meteoro simplemente sale del área local hacia la red.
     */
    private void destroyQuiet() {
        super.destroy();
    }

    /** * Realiza la destrucción completa del meteoro.
     * Este método se activa al recibir un impacto, otorgando puntaje al jugador
     * y activando la lógica de división en meteoros más pequeños.
     */
    @Override
    public void destroy() {
        gameState.divideMeteor(this);
        gameState.addScore(Constants.METEOR_SCORE, position);
        super.destroy();
    }

    /**
     * Renderiza el meteoro aplicando una rotación continua sobre su centro.
     * * @param g Contexto gráfico para el dibujo.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2, height / 2);
        g2d.drawImage(texture, at, null);
    }

    /**
     * Obtiene el tamaño actual del meteoro.
     * * @return El valor del enumerador {@link Size}.
     */
    public Size getSize() { return size; }
}