package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.UdpClient;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * La clase Laser representa el proyectil disparado por los objetos en el juego.
 * Implementa una lógica de proyectil que puede viajar entre diferentes instancias
 * de juego a través de la red y posee un periodo de gracia para evitar colisiones
 * con el emisor original.
 * * @author Gemini
 * @version 1.0
 */
public class Laser extends MovingObject {

    /** Número de frames iniciales en los que el láser no detecta colisiones. */
    private static final int IMMUNITY_FRAMES = 5;
    /** Contador actual de frames de inmunidad. */
    private int immunityFrames = IMMUNITY_FRAMES;
    /** Cliente UDP encargado de enviar el mensaje del láser si cruza los límites laterales. */
    private UdpClient udpClient;

    /**
     * Construye un nuevo Laser con posición, velocidad y ángulo definidos.
     * * @param position Coordenadas iniciales.
     * @param velocity Vector de dirección del disparo.
     * @param maxVel   Magnitud de la velocidad (rapidez).
     * @param angle    Ángulo de rotación para el renderizado.
     * @param texture  Imagen visual del láser.
     * @param gameState Estado actual del juego al que pertenece.
     */
    public Laser(Vector2D position, Vector2D velocity, double maxVel,
                 double angle, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        this.angle    = angle;
        this.velocity = velocity.scale(maxVel);
    }

    /**
     * Establece el cliente UDP necesario para la sincronización en red.
     * * @param client Instancia de UdpClient activa.
     */
    public void setUdpClient(UdpClient client) { this.udpClient = client; }

    /**
     * Actualiza la posición del láser, gestiona su salida de la pantalla (ya sea
     * para destruirse o para transferirse al otro PC) y procesa la lógica de colisión.
     */
    @Override
    public void update() {
        position = position.add(velocity);

        // Izquierda/derecha: transferir al otro PC
        if (position.getX() > Constants.WIDTH) {
            sendCross("RIGHT");
            destroy();
            return;
        }
        if (position.getX() < -width) {
            sendCross("LEFT");
            destroy();
            return;
        }

        // Arriba/abajo: destruir normal
        if (position.getY() < 0 || position.getY() > Constants.HEIGHT) {
            destroy();
            return;
        }

        // Inmunidad inicial: evita que el laser mate al jugador que lo disparó
        if (immunityFrames > 0) {
            immunityFrames--;
            return;
        }

        collidesWith();
    }

    /**
     * Envía un mensaje de red indicando que el láser ha cruzado un límite lateral
     * para que aparezca en la pantalla del equipo remoto.
     * * @param side El lado por el cual salió el proyectil ("LEFT" o "RIGHT").
     */
    private void sendCross(String side) {
        if (udpClient == null) return;
        udpClient.send(new GameMessage(MessageType.LASER_CROSS,
                side,
                String.valueOf(position.getY()),
                String.valueOf(velocity.getX()),
                String.valueOf(velocity.getY()),
                String.valueOf(angle)));
    }

    /**
     * Dibuja el láser en pantalla aplicando la transformación de rotación
     * basada en su ángulo de trayectoria.
     * * @param g Contexto gráfico para el renderizado.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());
        at.rotate(angle, width / 2, 0);
        g2d.drawImage(texture, at, null);
    }

    /**
     * Calcula el centro geométrico del láser para la detección de colisiones.
     * * @return Vector2D que representa el centro del proyectil.
     */
    @Override
    public Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + width / 2);
    }
}